/*
实现了一个简单的多核内存锁，它可以通过检查每个核心请求的锁和解锁来管理对内存的访问。
如果某个核心请求锁定并且没有其他核心已经锁定内存，
则该核心被授予内存锁定权限，并且其他核心的请求将被拒绝。

内存锁定状态由一个锁定标志和一个锁定所有者标识符来跟踪，
这个模块还通过io接口向每个核心发出一个锁定授权信号来通知核心是否已经获得了内存锁定权限。

*/
import chisel3._
import chisel3.util._

class MemoryLock(numCores:Int) extends Module {
    val io = IO(new Bundle {
        val requestLock = Vec(numCores, Input(Bool()))
        val requestUnlock = Vec(numCores, Input(Bool()))
        val lockGranted = Vec(numCores, Output(Bool()))
    })

    val lock = RegInit(false.B)
    val lockOwner = RegInit(0.U(log2Ceil(numCores).W))

    for(i <- 0 until numCores) {
        io.lockGranted(i) := false.B

        when(io.requestLock(i) && !lock) {
            lock := true.B
            lockOwner := i.U
            io.lockGranted(i) := true.B
        }

        when(io.requestUnlock(i) && lockOwner === i.U) {
            lock := false.B
        }
    }
}
