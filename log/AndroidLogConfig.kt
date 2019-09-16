package com.cell_tower.log

class AndroidLogConfig
{
    companion object
    {
        private var logSize: Long? = null

        private var isWriteInFile: Boolean = false

        private var isLogEnabled: Boolean = false
    }


    init
    {
        logSize = 1024 * 5
        isWriteInFile = false
        isLogEnabled = false
    }

    fun getLogSize(): Long?
    {
        return logSize
    }

    fun setLogSize(size: Long?)
    {
        logSize = size
    }

    fun isWriteInFile(): Boolean
    {
        return isWriteInFile
    }

    fun isLogEnabled(): Boolean
    {
        return isLogEnabled
    }

    fun setIsLogEnabled(logEnabled: Boolean)
    {
        isLogEnabled = logEnabled
    }

    fun setIsWriteInFile(writeInFile: Boolean)
    {
        isWriteInFile = writeInFile
    }


}
