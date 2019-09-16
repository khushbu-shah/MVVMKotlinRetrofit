package com.cell_tower.activity

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.cell_tower.R
import com.cell_tower.adapter.LocationDetailAdapter
import com.cell_tower.adapter.LocationDevicesAdapter
import com.cell_tower.utils.AppPreferences
import com.cell_tower.utils.CommonUtils
import com.cell_tower.utils.DialogUtils
import com.cell_tower.viewmodel.LocationDetailViewModel
import com.cell_tower.webservices.responsepojos.*
import com.cell_tower.webservices.responsepojos.WeatherPojo.WeatherResponse
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import android.location.Location
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationListener
import com.google.android.gms.common.GoogleApiAvailability
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.bumptech.glide.Glide
import com.cell_tower.common.MessageEvent
import com.cell_tower.databinding.ActivityLocationDetailsBinding
import com.cell_tower.interfaces.OnItemClick
import kotlinx.android.synthetic.main.common_layout.*
import org.eclipse.paho.android.service.MqttTraceHandler
import org.eclipse.paho.client.mqttv3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat


/*https://medium.com/@ssaurel/getting-gps-location-on-android-with-fused-location-provider-api-1001eb549089*/
class LocationDetailActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener,  MqttCallbackExtended, MqttCallback,
    MqttTraceHandler {

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (location != null) {

            setLatLonToPreference(location!!)
//            Log.e("TAG","Latitude : " + location!!.latitude + "\nLongitude : " + location!!.longitude)
        }
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {
        if (location != null) {
            setLatLonToPreference(location!!)
//            Log.e("TAG","Latitude : " + location!!.latitude + "\nLongitude : " + location!!.longitude)
        }
    }

    private var locationDetailViewModel: LocationDetailViewModel? = null

    private var location: Location? = null

    var locationBean =  LocationListResponse.Data.Locations()

    private var googleApiClient: GoogleApiClient? = null

    private var locationRequest: LocationRequest? = null

    private val PLAY_SERVICES_RESOLUTION_REQUEST : Int = 9000

    private val  UPDATE_INTERVAL : Long= 5000
    private val  FASTEST_INTERVAL : Long = 5000 // = 5 seconds

    var locationDetailBinding: ActivityLocationDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationDetailBinding = DataBindingUtil.setContentView(this, com.cell_tower.R.layout.activity_location_details)

        imgBack.visibility = View.VISIBLE

        locationDetailBinding!!.commonLayout.clickListener = this

        locationDetailViewModel =
            ViewModelProviders.of(this@LocationDetailActivity).get(LocationDetailViewModel::class.java)

        locationDetailViewModel!!.locationDetailResponse.observe(this, LocationDetailObserver())

        locationDetailViewModel!!.alertListResponse.observe(this,AlertListObserver())

        locationDetailViewModel!!.assetsResponse.observe(this, AssetsListObserver())

        locationDetailViewModel!!.weatherResponse.observe(this,WeatherDataObserver())

        locationBean = (intent.extras!!.get("Data") as? LocationListResponse.Data.Locations)!!

        locationDetailBinding!!.commonLayout.tvTitle.text = locationBean.locName

        imgBack.setOnClickListener(this)

        locationDetailBinding!!.tvPowerValue.text = "-"
        locationDetailBinding!!.tvFuelValue.text = "-"
        locationDetailBinding!!.tvBattery.text = "-"

        DialogUtils.showLoadingDialog(this)

        locationDetailViewModel!!.callLocationDetail(
            AppPreferences.getString(AppPreferences.AUTH_TOKEN, "", this)!!,
            locationBean.id
        )

        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
    }

    override fun onStart() {
        super.onStart()
        if (googleApiClient != null) {
            googleApiClient!!.connect()
        }

        try {
            if(!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);

        }catch (e: Exception){}

    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.e("TAG"," LocationDetailActivity : onDestroy ")

        try {
            EventBus.getDefault().unregister(this);
        }catch (e: Exception){}
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgBack -> {
                onBackPressed()
            }
            R.id.tvProfileUserName -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.imgProfile -> {
                locationDetailBinding!!.commonLayout.tvProfileUserName.performClick()
            }
            R.id.imgLogout -> {

                val builder = AlertDialog.Builder(this@LocationDetailActivity)

                // Set the alert dialog title
//                builder.setTitle("App background color")

                // Display a message on alert dialog
                builder.setMessage("Are you sure you want to logout?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do something when user press the positive button
                    AppPreferences.clearAllPreferences(this)
                    startActivity(Intent(this@LocationDetailActivity, LoginActivity::class.java))
                }

                // Display a negative button on alert dialog
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (locationDetailBinding!! != null)
            locationDetailBinding!!.userInfo = AppPreferences.getUserInfo(this)

        if(locationDetailBinding!!.locationBean != null)
        {
            locationDetailViewModel!!.callAssetList(
                AppPreferences.getString(
                    AppPreferences.AUTH_TOKEN,
                    "",
                    this@LocationDetailActivity
                )!!, locationDetailBinding!!.locationBean!!.id
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // stop location updates
        if (googleApiClient != null  &&  googleApiClient!!.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            googleApiClient!!.disconnect()
        }
    }

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
    }

    // Get MQTT connection and result data
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:MessageEvent) {
        if(event != null) {
            var mqttMessage = event.mqttMessage.toString()

            var jsonObject: JSONObject = JSONObject(mqttMessage)

            var strDeviceId = jsonObject.optString("deviceId")

//        var deviceId = "59f4ag"
            var deviceId = locationBean.towerdevice
            if (strDeviceId.equals(deviceId)) {
//        if (strDeviceId.equals("1wx5t3",true)) {
                var iterator: Iterator<String> = jsonObject.keys();
                while (iterator.hasNext()) {
                    var key = iterator.next();
                    when {
                        key.equals("Time") -> Log.i("TAG", "key:" + key + "--Value::" + jsonObject.optString(key))
                        key.equals("deviceId") -> Log.i("TAG", "key:" + key + "--Value::" + jsonObject.optString(key)
                        )
                        else -> when {
                            key.equals("PowerUsage", true) ->
                                locationDetailBinding!!.tvPowerValue.text =
                                    "${DecimalFormat("###.##").format(jsonObject.optString(key).toDouble())} KW"
                            key.equals("BatteryUsage", true) ->
                                locationDetailBinding!!.tvBattery.text =
                                    "${DecimalFormat("###.##").format(jsonObject.optString(key).toDouble())} V"
                            else ->
                                locationDetailBinding!!.tvFuelValue.text =
                                    "${DecimalFormat("###.##").format(jsonObject.optString(key).toDouble())} Gal"
                        }
                    }
                }
            }
        }
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
//        Log.e("TAG", "Message is ${message.toString()} topic is $topic")
        EventBus.getDefault().post(MessageEvent(topic,message))
    }

    override fun connectionLost(cause: Throwable?) {
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }

    override fun traceDebug(tag: String?, message: String?) {
//        Log.e("TAG", "Message is traceDebug $tag.toString()  is $message");
    }

    override fun traceException(tag: String?, message: String?, e: Exception?) {
//        Log.e("TAG", "traceException is $message.toString() topic is $tag exception is ${e!!.message.toString()}");
    }

    override fun traceError(tag: String?, message: String?) {
//        Log.e("TAG", "traceError is $message.toString() topic is $tag");
    }

    private fun setLatLonToPreference(location: Location) {
        var lat = this.location!!.latitude
        var lon = this.location!!.longitude

        AppPreferences.setDouble(AppPreferences.LAT, lat, this@LocationDetailActivity)
        AppPreferences.setDouble(AppPreferences.LON, lon, this@LocationDetailActivity)
    }

    /*Check for Play Service Availability*/
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
            } else {
                finish()
            }
            return false
        }
        return true
    }

    /*Current Location Updates*/
    private fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = UPDATE_INTERVAL
        locationRequest!!.fastestInterval = FASTEST_INTERVAL

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show()
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    /*Start Specification Response*/
    inner class LocationDetailObserver : Observer<LocationDetailResponse> {
        override fun onChanged(response: LocationDetailResponse?) {

            if (response!!.status == 200) {
                locationDetailBinding!!.locationBean = response.data.locations
            }
                locationDetailViewModel!!.callAlertList(AppPreferences.getString(AppPreferences.AUTH_TOKEN,"",this@LocationDetailActivity)!!,locationBean.id)
        }
    }
    /*End Specification Response*/

    /*Start Alerts Service Response*/
    inner class AlertListObserver : Observer<AlertListResponse>
    {
        override fun onChanged(response: AlertListResponse?) {

            var dayMap = HashMap<Int, ArrayList<AlertListResponse.Data.LocationTower>>()
            if (response!!.status == 200) {
                val alAlerts = response.data.locationTower
                val alBean = ArrayList<AlertListResponse.Data.LocationTower>()

                /*Put data to hashmap for display data : by  Day difference */

                if(alAlerts != null &&  alAlerts.size > 0) {

                    for (bean in alAlerts) {
                        bean.dayDiff = CommonUtils.getDayDifference(bean)
                        if (dayMap.containsKey(bean.dayDiff)) {
                            bean.isDisplay = false
                            var s =alAlerts.find { it.createdAt.equals(bean.createdAt) }
                            alBean.add(s!!)
                            dayMap.put(bean.dayDiff, alBean)
                        } else {
                            bean.isDisplay = true
                            alBean.add(bean)
                            dayMap.put(bean.dayDiff,alBean)
                        }
                        if(alBean.size > 0)
                            alBean.sortedWith(compareBy {it.dayDiff})
                    }

                    dayMap.toSortedMap()

                    var adapter = LocationDetailAdapter(dayMap, this@LocationDetailActivity)
                    locationDetailBinding!!.rvAlertDetails.adapter = adapter
                }
                else
                {
                    locationDetailBinding!!.inNoDataAlert.isVisible = true
                    locationDetailBinding!!.text = "No Alerts available"
                }
            }
                locationDetailViewModel!!.callAssetList(
                    AppPreferences.getString(
                        AppPreferences.AUTH_TOKEN,
                        "",
                        this@LocationDetailActivity
                    )!!, locationDetailBinding!!.locationBean!!.id
                )
        }
    }
    /*End Alerts Service Response*/

    inner class AssetsListObserver : Observer<AssetsResponse> {
        override fun onChanged(response: AssetsResponse?) {

            if (response!!.status == 200) {
                var alAlerts = response.data.location
                if(alAlerts != null)
                {
                    var alLocationDevice = ArrayList<AssetsResponse.Data.Location.LocationDevice>()

                    alLocationDevice = alAlerts.LocationDevices

                    var adapter = LocationDevicesAdapter(alLocationDevice, this@LocationDetailActivity,object : OnItemClick{
                        override fun onImageClick(bean: Any) {

                            bean as AssetsResponse.Data.Location.LocationDevice

                            var intent = Intent(this@LocationDetailActivity,TowerDetailActivity::class.java)
                            bean.towerName = locationDetailBinding!!.commonLayout.tvTitle.text.toString()
                            intent.putExtra("Data", bean)
                            startActivity(intent)
                        }
                    })
                    locationDetailBinding!!.rvAssetsList.adapter = adapter
                }
                else
                {
                    locationDetailBinding!!.inNoData.isVisible = true
                    locationDetailBinding!!.text = "No Assets available"
                }
            }
                locationDetailViewModel!!.callWeatherData()
            if(DialogUtils.isLoadingDialogShowing())
                DialogUtils.hideDialog()
        }
    }

    /*Start Weather Response*/
    inner class WeatherDataObserver : Observer<WeatherResponse> {
        override fun onChanged(response: WeatherResponse?) {

            if (response!!.cod == 200) {

                locationDetailBinding!!.tvWind.text =  "${response.wind.speed} kmph"
                locationDetailBinding!!.tvHumidity.text =  "${response.main.humidity}%"
                locationDetailBinding!!.tvWeather.text =  "${response.weather[0].main}"
                locationDetailBinding!!.weatherImgId =  "${response.weather[0].icon}"

                var tmp = response.main.temp

                var farnahet = (tmp - 273.15) * 9/5 + 32

                locationDetailBinding!!.tvCityTemp.text =  "${DecimalFormat("##.##").format(farnahet)} F"

                Glide.with(this@LocationDetailActivity).load(String.format(resources.getString(R.string.weather_image_url), response.weather[0].icon)).into(locationDetailBinding!!.ivWeather)
            }
            if(DialogUtils.isLoadingDialogShowing())
                DialogUtils.hideDialog()
        }
    }
    /*End Weather Response*/
}
