package com.github.kr328.clash

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.os.Parcel
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import android.util.Log
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.ClashProcess
import java.io.FileDescriptor
import kotlin.concurrent.thread

class ClashService : Service() {
    companion object {
        private val TAG = "ClashForAndroid"
    }

    private lateinit var clash: Clash
    private inner class ClashServiceImpl : IClashService.Stub() {
        override fun start() {
            clash.start()
        }

        override fun stop() {
            clash.stop()
        }

        override fun loadProfile(url: Uri?) {
            clash.loadProfile(url ?: throw RemoteException())
        }

        override fun startTunDevice(fd: ParcelFileDescriptor, mtu: Int) {
            clash.startTunDevice(fd.fileDescriptor, mtu)
        }
    }

    override fun onCreate() {
        super.onCreate()

        clash = Clash(this,
            filesDir.resolve("clash"),
            cacheDir.resolve("clash_controller")) {
            
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ClashServiceImpl()
    }

    override fun onDestroy() {
        clash.stop()

        super.onDestroy()
    }
}