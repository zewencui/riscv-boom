import chisel3._
import chisel3.util._

class MemoryLock(numCores:Int) extends Module {
    val io = IO(new Bundle {
        val requestLock = Vec(numCoresm, Input(Bool()))
        val requestUnlock = Vec(numCores, Input(Bool()))
        val lockGranted = Vec(numCores, Output(Bool()))
    })

    val lock = RegInit(false.B)
    val lockOwner = RegInit(0.U(log2Ceil(numCores).W))

    for(i <- 0 until numCores) {
        io.lockGranted(i) := false.B

        when(io.requestLock(i) && !locked) {
            locked := true.B
            lockOwner := i.U
            io.lockGranted(i) := true.B
        }

        when(io.requestUnlock(i) && lockOwner === i.U) {
            lock := false.B
        }
    }
}