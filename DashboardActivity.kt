package com.cell_tower.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cell_tower.utils.AppPreferences
import com.cell_tower.adapter.StatisticsDetailAdapter
import com.cell_tower.bean.StisticsBean
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.widget.Toast
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.widget.AdapterView
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import com.cell_tower.adapter.SpinAdapter
import com.cell_tower.viewmodel.DashboardViewModel
import com.cell_tower.webservices.responsepojos.DashboardCountResponse
import com.cell_tower.webservices.responsepojos.LocationListResponse
import com.google.android.gms.maps.model.*
import com.cell_tower.R
import com.cell_tower.utils.DialogUtils
import com.google.android.gms.maps.model.CameraPosition
import android.content.DialogInterface
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import com.cell_tower.log.AndroidLog
import android.content.pm.PackageManager
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import com.cell_tower.azure.MyHandler
import com.cell_tower.azure.RegistrationIntentService
import com.cell_tower.common.MessageEvent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.common_layout.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.greenrobot.eventbus.EventBus
import java.util.*

/*https://basememara.com/storing-multidimensional-resource-arrays-in-android*/
open class DashboardActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object
    {
        var mqttAndroidClient: MqttAndroidClient? = null
    }


    val markerOptions = MarkerOptions()

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    private val TAG = "DashboardActivity"

    var dashboardBinding: com.cell_tower.databinding.ActivityDashboardBinding? = null

    private val PERMISSION_REQUEST_CODE = 100;

    private var mMap: GoogleMap? = null

    private var hashMapStastics: HashMap<Int, StisticsBean> = HashMap<Int, StisticsBean>()

    private var statisticsDetailAdapter: StatisticsDetailAdapter? = null

    private var dashboardViewModel : DashboardViewModel ?= null

    var alLocation = ArrayList<LocationListResponse.Data.Locations>()

//    var clientId = "CellTower_Android"
    var clientId = MqttClient.generateClientId()

    private var subscribeTopic = "sensorData"

    var isSpinnerAdapterBind = false

    var firstVisiblePosition : Int = 0

    private var programaticallyScrolled: Boolean = false

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dashboardBinding = DataBindingUtil.setContentView(this, com.cell_tower.R.layout.activity_dashboard)
        dashboardBinding!!.includeLayout.clickListener = this
        dashboardBinding!!.clickListener = this

        imgBack.visibility = View.GONE

        dashboardBinding!!.userInfo = AppPreferences.getUserInfo(this)

        dashboardViewModel = ViewModelProviders.of(this@DashboardActivity).get(DashboardViewModel::class.java)


        registerWithNotificationHubs()
        MyHandler.createChannelAndHandleNotifications(getApplicationContext())

        MqttClient.generateClientId()

//        var uri = "tcp://107.180.68.38:1883"
        var uri = "tcp://107.180.68.38"

        var  filePersistence : MqttDefaultFilePersistence =  MqttDefaultFilePersistence(getDir("mqtt", MODE_PRIVATE).getAbsolutePath());
//            MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), MQTT_BROKER_HOST, clientId,filePersistence);

        mqttAndroidClient = MqttAndroidClient(this@DashboardActivity, uri, clientId, filePersistence)
        MqttDataLoad()

        if (!checkPermission()) {
            requestPermission()
        } else {
            val mapFragment = supportFragmentManager.findFragmentById(com.cell_tower.R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            DialogUtils.showLoadingDialog(this)

            dashboardViewModel!!.callDashboardCount(AppPreferences.getString(AppPreferences.AUTH_TOKEN, "", this)!!)

            dashboardViewModel!!.errorResponse.observe(this, DashboardResponseErrorObserver())
            dashboardViewModel!!.dashboardCountResponse.observe(this, DashboardResponseObserver())

//        var mCategories = ArrayList<TmpCategoryBean>()
            setDetailView()

        }
    }

    // Registration for Azure Push Notification
    fun registerWithNotificationHubs() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show()
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.")
                finish()
            }
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()

        if(dashboardBinding!! != null)
            dashboardBinding!!.userInfo = AppPreferences.getUserInfo(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    // Map Location Click Listener
    private val country_listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            if(position != 0)
            {
                /*reload Markers in MAP */
                var strSelectedItem = dashboardBinding!!.spnCountryList.selectedItem.toString()
                var data = alLocation.find { it.locName.equals(strSelectedItem) } as LocationListResponse.Data.Locations
//                var location = alLocation[position]

                val coordinate = LatLng(data.locLatitude.toDouble(), data.locLongitude.toDouble())
                val cameraPosition = CameraPosition.Builder()
                    .target(coordinate)      // Sets the center of the map to Mountain View
                    .zoom(17f)                   // Sets the zoom
                    .bearing(90f)                // Sets the orientation of the camera to east
                    .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
                    .build()                   // Creates a CameraPosition from the builder
                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
            else if(position == 0)
            {
                /*Reload Markers if Select All Tower from Spinner*/
                if (markerOptions != null) {
                    mMap!!.clear()
                }

                for(i in 0..(alLocation.size-1))
                {
                    var bean = alLocation[i]

                    /*Map Integrate*/
                    if(bean.locLatitude != "")
                    {
//                        val markerOptions = MarkerOptions()
                        markerOptions.position(LatLng(bean.locLatitude.toDouble(), bean.locLongitude.toDouble()))
                        markerOptions.title(bean.locName)
                        val markerChicago: Marker?  = mMap!!.addMarker(markerOptions)

                        val bitmapDescriptorChicago : BitmapDescriptor = if(bean.status == 0)
                            BitmapDescriptorFactory.fromResource(com.cell_tower.R.drawable.ic_tower_orange)
                        else
                            BitmapDescriptorFactory.fromResource(com.cell_tower.R.drawable.ic_tower_green)

                        markerChicago!!.setIcon(bitmapDescriptorChicago)
                        markerChicago!!.tag = 0

                        mMap?.let {
                            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(bean.locLatitude.toDouble(), bean.locLongitude.toDouble())))
                        }
                        mMap!!.setOnMarkerClickListener(this@DashboardActivity);
                    }

                    val coordinate = LatLng(alLocation[alLocation.size-1].locLatitude.toDouble(), alLocation[alLocation.size-1].locLongitude.toDouble())
                    val cameraPosition = CameraPosition.Builder()
                        .target(coordinate)      // Sets the center of the map to Mountain View
                        .build()
                    mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.tvProfileUserName -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.imgProfile -> {
                dashboardBinding!!.includeLayout.tvProfileUserName.performClick()
            }
            R.id.imgLogout -> {
                val builder = AlertDialog.Builder(this@DashboardActivity)

                // Set the alert dialog title
//                builder.setTitle("App background color")

                // Display a message on alert dialog
                builder.setMessage("Are you sure you want to logout?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){dialog, which ->
                    // Do something when user press the positive button
                    AppPreferences.clearAllPreferences(this)
                    startActivity(Intent(this@DashboardActivity,LoginActivity::class.java))
                }

                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){dialog,which ->
                    dialog.dismiss()
                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            }
        }
    }

    // Map Marker Click
    override fun onMarkerClick(marker: Marker?): Boolean {
        // Retrieve the data from the marker.
        var clickCount: Int? = marker!!.tag as Int

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount!! + 1
            marker.tag = clickCount
        }

        for(bean in alLocation) {
            if (marker!!.title.equals(bean.locName, true)) {
                var intent = Intent(this,LocationDetailActivity::class.java)
                intent.putExtra("Data", bean)
                startActivity(intent)
            }
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    private fun setDetailView() {

        if (statisticsDetailAdapter == null) {
//            rvTotalInfo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvTotalInfo.layoutManager = GridLayoutManager(this,1, GridLayoutManager.HORIZONTAL, false)
            statisticsDetailAdapter = StatisticsDetailAdapter(loadStisticsMap())

            //statisticsDetailAdapter = StatisticsDetailAdapter()
            statisticsDetailAdapter!!.setOnItemClick(object : StatisticsDetailAdapter.OnItemClick {
                override fun onImageClick(position: Int) {
//                    startActivity(Intent(this@DashboardActivity,TowerDetailActivity::class.java))
                }
            })
            rvTotalInfo.adapter = statisticsDetailAdapter
        } else {
            //statisticsDetailAdapter!!.notifyDataSetChanged()
        }
    }

    // Set Overall Statistics of Different Total items
    private fun loadStisticsMap(): HashMap<Int, StisticsBean>
    {
        if (hashMapStastics.size == 0) {

            var statsicsBean = StisticsBean()
            statsicsBean.backgroundColor = resources.getColor(com.cell_tower.R.color.colorTowerBox)
            statsicsBean.staticsName = "Total Towers"
            statsicsBean.image = resources.getDrawable(com.cell_tower.R.drawable.box_icon_tower)

            hashMapStastics.put(1, statsicsBean)

            statsicsBean = StisticsBean()
            statsicsBean.backgroundColor = resources.getColor(com.cell_tower.R.color.colorAssetsBox)
            statsicsBean.staticsName = "Total Assests"
            statsicsBean.image = resources.getDrawable(com.cell_tower.R.drawable.box_icon_assets)

            hashMapStastics.put(2, statsicsBean)

            statsicsBean = StisticsBean()
            statsicsBean.backgroundColor = resources.getColor(com.cell_tower.R.color.colorUsersBox)
            statsicsBean.staticsName = "Total Users"
            statsicsBean.image = resources.getDrawable(com.cell_tower.R.drawable.box_icon_users)

            hashMapStastics.put(3, statsicsBean)

            statsicsBean = StisticsBean()
            statsicsBean.backgroundColor = resources.getColor(com.cell_tower.R.color.colorTicketsBox)
            statsicsBean.staticsName = "Total Tickets"
            statsicsBean.image = resources.getDrawable(com.cell_tower.R.drawable.box_icon_tickets)

            hashMapStastics.put(4, statsicsBean)

            statsicsBean = StisticsBean()
            statsicsBean.backgroundColor = resources.getColor(com.cell_tower.R.color.colorAlertsBox)
            statsicsBean.staticsName = "Total Alerts"
            statsicsBean.image = resources.getDrawable(com.cell_tower.R.drawable.box_icon_alerts)

            hashMapStastics.put(5, statsicsBean)

            return hashMapStastics
        } else {
            return hashMapStastics
        }
    }

    // Set Total Count in Overall Statistics
    inner class DashboardResponseObserver : Observer<DashboardCountResponse>
    {
        override fun onChanged(response: DashboardCountResponse?) {

            if(response!!.status == 200)
            {
                hashMapStastics[5]!!.statisticsCount = response!!.data.alerts
                hashMapStastics[4]!!.statisticsCount = response!!.data.tickets
                hashMapStastics[3]!!.statisticsCount= response!!.data.users.toString()
                hashMapStastics[2]!!.statisticsCount = response!!.data.assets
                hashMapStastics[1]!!.statisticsCount = response!!.data.towers.active+"/"+response!!.data.towers.total
//            hashMapStastics[1]!!.statisticsCount= response!!.data.towers.total

                if (statisticsDetailAdapter!= null )
//                statisticsDetailAdapter!!.setDashboard(hashMapStastics)
                    statisticsDetailAdapter!!.notifyDataSetChanged()
            }
            dashboardViewModel!!.locationListResponse.observe(this@DashboardActivity,LocationListObserver())
            dashboardViewModel!!.callLocationList(AppPreferences.getString(AppPreferences.AUTH_TOKEN,"",this@DashboardActivity)!!)
        }

    }

    inner class DashboardResponseErrorObserver : Observer<String>
    {
        override fun onChanged(message: String?) {
            Toast.makeText(this@DashboardActivity,message,Toast.LENGTH_SHORT).show()
        }
    }

    inner class LocationListObserver : Observer<LocationListResponse>
    {
        override fun onChanged(response: LocationListResponse?) {
            if(response!!.status == 200)
            {
                var locationAddress : ArrayList<String> = ArrayList<String>()

                locationAddress.add("All")

                alLocation.clear()

                alLocation.add(LocationListResponse.Data.Locations())

                for(bean in response!!.data.locations)
                    alLocation.add(bean)

                for(i in 0..(alLocation.size-1))
                {
                    var bean = alLocation[i]

                    /*Map Integrate*/
                    if(bean.locLatitude != "")
                    {
//                        val markerOptions = MarkerOptions()
                        markerOptions.position(LatLng(bean.locLatitude.toDouble(), bean.locLongitude.toDouble()))
                        markerOptions.title(bean.locName)
                        val markerChicago: Marker?  = mMap!!.addMarker(markerOptions)

                        val bitmapDescriptorChicago : BitmapDescriptor = if(bean.status == 0)
                            BitmapDescriptorFactory.fromResource(com.cell_tower.R.drawable.ic_tower_orange)
                        else
                            BitmapDescriptorFactory.fromResource(com.cell_tower.R.drawable.ic_tower_green)

                        markerChicago!!.setIcon(bitmapDescriptorChicago)
                        markerChicago!!.tag = 0

                        mMap?.let {
                            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(bean.locLatitude.toDouble(), bean.locLongitude.toDouble())))
                        }

                        mMap!!.setOnMarkerClickListener(this@DashboardActivity);

                        /*Spinner Integrate*/
                        locationAddress.add(bean.locName)
                    }

                }

//                locationAddress.sort()
                var adapter : SpinAdapter =  SpinAdapter(this@DashboardActivity, com.cell_tower.R.layout.row_location,
                    com.cell_tower.R.id.tvTowerName, locationAddress)
                dashboardBinding!!.spnCountryList.adapter = adapter
                isSpinnerAdapterBind = true

                dashboardBinding!!.spnCountryList.onItemSelectedListener = country_listener
            }
            if(DialogUtils.isLoadingDialogShowing())
                DialogUtils.hideDialog()
            }
        }

    // MQTT Connection Code
    private fun MqttDataLoad() {

        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.keepAliveInterval
        mqttConnectOptions.isCleanSession = true

        try {
            mqttAndroidClient!!.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {

                    Log.e("TEst", "Connect sucess============>")

                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    mqttAndroidClient!!.setBufferOpts(disconnectedBufferOptions)

                    subscribeToTopic()
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {

                    Log.e("TEst", "Connect Fail ${exception!!.message}")
                }
            }
            )
            mqttAndroidClient!!.setCallback(object  : MqttCallback{
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.e("TAG", "Message is ${message.toString()} topic is $topic")
                    EventBus.getDefault().post(MessageEvent(topic,message))
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e("LocationDetailActivity","connectionLost")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.e("LocationDetailActivity","deliveryComplete")
                }
            })
        } catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }

    // MQTT Topic Subscribe
    fun subscribeToTopic() {
        try {
            mqttAndroidClient!!.subscribe(subscribeTopic, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("TEst", "subscribe sucess============>")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("TEst", "subscribe Fail ${exception!!.message}")
                }
            })
        } catch (ex: MqttException) {
            System.err.println("Exception whilst subscribing")
            ex.printStackTrace()
        }
    }

    /**
     * RUN TIME PERMISSION
     *
     * @return
     */
    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)

            return (result == PackageManager.PERMISSION_GRANTED)
        } else {
            //permission is automatically granted on sdk<23 upon installation
                return true
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted ) {
//                    AndroidLog.v("Main Activity ==>", "All permission Granted")
//                    submitData()

                    val mapFragment = supportFragmentManager.findFragmentById(com.cell_tower.R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)

                    DialogUtils.showLoadingDialog(this)

                    dashboardViewModel!!.callDashboardCount(AppPreferences.getString(AppPreferences.AUTH_TOKEN,"",this)!!)

                    dashboardViewModel!!.errorResponse.observe(this,DashboardResponseErrorObserver())
                    dashboardViewModel!!.dashboardCountResponse.observe(this,DashboardResponseObserver())

//        var mCategories = ArrayList<TmpCategoryBean>()
                    setDetailView()

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel(getString(R.string.str_alert_permissions),
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(ACCESS_FINE_LOCATION),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mqttAndroidClient != null && mqttAndroidClient!!.isConnected) {
            try {
//               mqttAndroidClient!!.unsubscribe(subscribeTopic)

                mqttAndroidClient!!.disconnect()

//                mqttAndroidClient!!.unregisterResources()
//                mqttAndroidClient!!.close()
                mqttAndroidClient = null
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@DashboardActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
//            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}