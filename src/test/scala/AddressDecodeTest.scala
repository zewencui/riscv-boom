import scala.util.Random

import org.scalatest._

import chisel3._
import chisel3.util._
import chisel3.iotesters._

import freechips.rocketchip.config.{Parameters}
import freechips.rocketchip.system._
import freechips.rocketchip.tile._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem._

class AddressDecoderTest(dut: AddressDecoder) extends PeekPokeTester(dut) {
  for {
    address <- 0 to 0xFFFFFFFF
  } {
    poke(dut.io.address, address)
    expect(dut.io.enable, address >= 0x1000 && address <= 0x2000)
  }
}

class AddressDecoderSpec extends FlatSpec with ChiselFlatSpec with Matchers{
  behavior of "AddressDecoder"
  it should "enable output when address is in the range" in {
    chisel3.iotesters.Driver(() => new AddressDecoder(0x1000.U, 0x2000.U)) { dut =>
      new AddressDecoderTest(dut)
    } should be (true)
  }
}
