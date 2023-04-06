import chisel3._
import chisel3.util._

class AddressAlignmentChecker extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(32.W))
    val size = Input(UInt(2.W))
    val aligned = Output(Bool())
  })

  val aligned = Wire(Bool())
  val addr = io.addr
  val size = io.size

  aligned := true.B   //字节对齐
  when (size === 0.U) {
    aligned := addr(0) === 0.U
  } .elsewhen (size === 1.U) { //半字对齐
    aligned := addr(1, 0) === 0.U
  } .elsewhen (size === 2.U) { //字对齐
    aligned := addr(2, 0) === 0.U
  } .elsewhen (size === 3.U) { //双字对齐
    aligned := addr(3, 0) === 0.U
  }

  io.aligned := aligned
}