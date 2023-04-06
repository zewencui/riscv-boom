import chisel3._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}

class AddressAlignmentCheckerTest(dut: AddressAlignmentChecker) extends PeekPokeTester(dut) {
  for {
    size <- 0 to 3
    addr <- 0 to 255
  } {
    poke(dut.io.addr, addr)
    poke(dut.io.size, size)
    expect(dut.io.aligned, addr % (1 << (size + 1)) == 0)
  }
}

class AddressAlignmentCheckerSpec extends FlatSpec with Matchers {
  behavior of "AddressAlignmentChecker"
  it should "detect address alignment correctly" in {
    chisel3.iotesters.Driver(() => new AddressAlignmentChecker) { dut =>
      new AddressAlignmentCheckerTest(dut)
    } should be (true)
  }
}
