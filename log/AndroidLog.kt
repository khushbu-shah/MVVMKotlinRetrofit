package com.cell_tower.log

import android.content.Context
import android.util.Log
import com.cell_tower.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

open class AndroidLog protected constructor(var logConfig: AndroidLogConfig, var mContext: Context) {
    companion object {
        private val MSG_PREFIX = "[AndroidLog]"

        private val fileName = "LOG.txt"

        private var instance: AndroidLog? = null

        private var logFile: File? = null

        internal fun getLogFilePath(): String {
            if (logFile != null && logFile!!.exists()) {
                return logFile!!.path
            } else {
                createLogFile(fileName)
                return logFile!!.path
            }

        }

        internal fun create(androidLogConfig: AndroidLogConfig, context: Context) {
            if (instance == null) {
                instance = AndroidLog(androidLogConfig, context)
            }
        }

        fun printStackTrace(Tag: String, e: Exception?) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (e != null && e.stackTrace != null) {
                    var sb = StringBuilder()
                    val stackTraceElements = e.stackTrace

                    AndroidLog.e(
                        Tag,
                        StringBuilder().append("------ ").append(e.toString()).append(" -----").toString()
                    )


                    if (e.cause != null) {
                        val cause = e.cause
                        val causeErrorMessage = cause!!.message
                        AndroidLog.e(
                            Tag,
                            StringBuilder().append("------ ").append(causeErrorMessage).append(" -----").toString()
                        )
                    }

                    if (e.message != null) {
                        AndroidLog.e(
                            Tag,
                            StringBuilder().append("------ ").append(e.message).append(" -----").toString()
                        )
                    }

                    for (traceElement in stackTraceElements) {
                        sb.append(traceElement.className).append(" - ").append(traceElement.methodName).append(" - ")
                            .append(traceElement.lineNumber)

                        AndroidLog.e(Tag, sb.toString())
                        sb = StringBuilder()
                    }
                }
            }
        }


        fun v(tag: String, message: String, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.v(tag, MSG_PREFIX + message)
                }
            }
        }

        fun d(tag: String, message: String, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.d(tag, MSG_PREFIX + message)
                }
            }
        }

        fun i(tag: String, message: String, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.i(tag, MSG_PREFIX + message)
                }
            }
        }

        fun w(tag: String, message: String, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.w(tag, MSG_PREFIX + message)
                }
            }
        }

        fun e(tag: String, message: String, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.e(tag, MSG_PREFIX + message)
                }
            }
        }

        // Message plus throwables
        fun v(tag: String, message: String, tr: Throwable, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.v(tag, MSG_PREFIX + message, tr)
                }
            }
        }

        fun d(tag: String, message: String, tr: Throwable, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.d(tag, MSG_PREFIX + message, tr)
                }
            }
        }

        fun i(tag: String, message: String, tr: Throwable, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.i(tag, MSG_PREFIX + message, tr)
                }
            }
        }

        fun w(tag: String, message: String, tr: Throwable, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.w(tag, MSG_PREFIX + message, tr)
                }
            }
        }

        fun e(tag: String, message: String, tr: Throwable, isWrite: Boolean = false) {
            if (instance!!.logConfig.isLogEnabled()) {
                if (instance!!.logConfig.isWriteInFile()) {
                    appendLogFile(tag, message, isWrite)
                } else {
                    Log.e(tag, MSG_PREFIX + message, tr)
                }
            }
        }

        private fun appendLogFile(tag: String, message: String, isWrite: Boolean) {
            if (instance!!.logConfig.isLogEnabled()) {
                Log.e(tag, MSG_PREFIX + message)
            }
            if (isWrite && instance!!.logConfig.isWriteInFile()) {
                if (logFile == null) {
                    createLogFile(fileName)
                }
//                else
//                {
//                    //Per day log only....
//                    val cDate = DateTimeUtils.getCalDateToString(Calendar.getInstance(), DateFormats.MM_DD_YYYY)
//                    val lastDate = SharedPref.instance!!.getValueFromPreferences(instance!!.mContext.getString(R.string.pref_current_date))
//                    if(cDate!=lastDate)
//                    {
//                        SharedPref.instance!!.saveStringSession(instance!!.mContext.getString(R.string.pref_current_date), cDate)
//                        recreateFile()
//                        writeLogFile(tag, message)
//                    }
//                }

                if (!checkFileSizeExceeds()) {
                    //writeLogFile(tag, message)
                } else {
                    recreateFile()
                    //writeLogFile(tag, message)
                }
            }
        }

        private fun recreateFile() {
            try {
                if (logFile != null && logFile!!.exists()) {
                    logFile!!.delete()
                    logFile!!.createNewFile()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /*private fun writeLogFile(tag: String, message: String) {
            if (logFile != null) {
                try {
                    val buf = BufferedWriter(FileWriter(logFile!!, true))
                    buf.append("[$date]\t\t[$tag]\t\t$message\r\n")
                    buf.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }*/

        @Synchronized
        private fun checkFileSizeExceeds(): Boolean {
            if (logFile != null && logFile!!.exists()) {
                val fileLength = logFile!!.length()

                return fileLength > instance!!.logConfig.getLogSize()!!
            }
            return false
        }


        @Synchronized
        private fun createLogFile(fileName: String) {
            try {
                val logDir = File(
                    instance!!.mContext.getExternalFilesDir(null),
                    instance!!.mContext.getString(R.string.app_name)
                )

                if (!logDir.exists()) {
                    logDir.mkdir()
                }



                logFile = File(logDir.path + File.separator + fileName)

                if (!logFile!!.exists()) {
                    logFile!!.createNewFile()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /*private val date: String
            get() {
                val parsePattern = DateFormats.ANDROID_LOG_FORMAT
                val dateParser = SimpleDateFormat(parsePattern)
                return dateParser.format(Date(System.currentTimeMillis()))
            }*/
    }

}