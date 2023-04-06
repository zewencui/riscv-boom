import chisel3._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}

class ProtectionBitCheckerTest(dut: ProtectionBitChecker) extends PeekPokeTester(dut) {
  // 读保护
  poke(dut.io.isRead, true)
  poke(dut.io.isWrite, false)
  poke(dut.io.isExecute, false)
  poke(dut.io.protectionBits, 1)
  poke(dut.io.address, 0)
  expect(dut.io.accessError, false)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, true)
  poke(dut.io.protectionBits, 3)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, false)

  //write保护
  poke(dut.io.isRead, false)
  poke(dut.io.isWrite, true)
  poke(dut.io.isExecute, false)
  poke(dut.io.protectionBits, 2)
  poke(dut.io.address, 0)
  expect(dut.io.accessError, false)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, true)
  poke(dut.io.protectionBits, 3)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, false)

//execute保护
  poke(dut.io.isRead, false)
  poke(dut.io.isWrite, false)
  poke(dut.io.isExecute, true)
  poke(dut.io.protectionBits, 4)
  poke(dut.io.address, 0)
  expect(dut.io.accessError, false)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, true)
  poke(dut.io.protectionBits, 5)
  poke(dut.io.address, 4)
  expect(dut.io.accessError, false)
}

class ProtectionBitCheckerSpec extends FlatSpec with Matchers {
  behavior of "ProtectionBitChecker"
  it should "detect access errors based on protection bits" in {
    chisel3.iotesters.Driver(() => new ProtectionBitChecker) { dut =>
      new ProtectionBitCheckerTest(dut)
    } should be (true)
  }
}
