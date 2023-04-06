import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AddressAlignmentCheckerTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "AddressAlignmentChecker"

  it should "detect address alignment correctly" in {
    test(new AddressAlignmentChecker) { dut =>
      for {
        size <- 0 to 3
        addr <- 0 to 255
      } {
        dut.io.addr.poke(addr.U)
        dut.io.size.poke(size.U)
        dut.clock.step()
        dut.io.aligned.expect((addr % (1 << (size + 1))) === 0.U, s"addr: $addr, size: $size")
      }
    }
  }
}







/*
import chisel3._
import chisel3.experimental._
import org.scalatest.{FlatSpec, Matchers}

class AddressAlignmentCheckerTest(dut: AddressAlignmentChecker) extends ChiselFlatSpec (dut) {
  for {
    size <- 0 to 3
    addr <- 0 to 255
  } {
    poke(dut.io.addr, addr)
    poke(dut.io.size, size)
    expect(dut.io.aligned, addr % (1 << (size + 1)) == 0)
  }
}

class AddressAlignmentCheckerSpec extends FlatSpec with ChiselFlatSpec with Matchers{
  behavior of "AddressAlignmentChecker"
  it should "detect address alignment correctly" in {
    chisel3.iotesters.Driver(() => new AddressAlignmentChecker) { dut =>
      new AddressAlignmentCheckerTest(dut)
    } should be (true)
  }
}
*/