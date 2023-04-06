import chisel3._
import chisel3.util._

class AddressDecoder(startAdress: UInt, endAddress: UInt) extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(32.W))
    val enable = Output(Bool())
  })

  io.enable := io.address >= startAdress && io.address <= endAddress
}

object AddressDecoderMain extends App {
  chisel3.Driver.execute(args, () => new AddressDecoder(0x1000.U, 0x2000.U))  //TODO:
}
