import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import chisel3.stage.ChiselGeneratorAnnotation


class AddressDecoder(startAdress: UInt, endAddress: UInt) extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(32.W))
    val enable = Output(Bool())
  })

  io.enable := io.address >= startAdress && io.address <= endAddress
}

object AddressDecoderMain extends App {
  (new ChiselStage).execute(args, Seq(ChiselGeneratorAnnotation(() => new AddressDecoder(0x1000.U, 0x2000.U))))
}

