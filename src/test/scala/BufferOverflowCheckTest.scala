import chisel3._
import chisel3.tester._
import org.scalatest._
import chiseltest.internal.VerilatorBackendAnnotation

class BufferOverflowCheckTester(c: BufferOverflowCheck) extends PeekPokeTester(c) {
  val bufferSize = 16
  val boundaryOffset = 4

  val dataSeq = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
  val expectedOverflowSeq = Seq(false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true)
  val writeEnSeq = Seq(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)
  val readEnSeq = Seq(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false)

  reset(10)
  poke(c.io.writeEn, false.B)
  poke(c.io.readEn, false.B)
  poke(c.io.dataIn, 0.U)
  step(1)

  for (i <- dataSeq.indices) {
 
    poke(c.io.dataIn, dataSeq(i).toByte)
    poke(c.io.writeEn, writeEnSeq(i))
    poke(c.io.readEn, readEnSeq(i))
   
    step(1)
    val expectedOverflow = expectedOverflowSeq(i)
    val actualOverflow = peek(c.io.overflow)
    println(s"Step $i: dataIn=${peek(c.io.dataIn)}, writeEn=${peek(c.io.writeEn)}, readEn=${peek(c.io.readEn)}, overflow=$actualOverflow (expected=$expectedOverflow)")
    expect(c.io.overflow, expectedOverflow)
  }
}
/*

//简单的实现
class BufferOverflowCheckTest extends FlatSpec with ChiselScalatestTester with Matchers {

  behavior of "BufferOverflowDetect"

  it should "detect buffer overflow" in {

    test(new BufferOverflowCheck(16, 4)).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>

      // 向缓冲区写一些数据
      dut.io.writeEn.poke(true.B)
      for (i <- 0 until 16) {
        dut.io.dataIn.poke(i.U)
        dut.clock.step()
      }
      dut.io.writeEn.poke(false.B)

      //尝试读取缓冲区边界内的数据
      dut.io.readEn.poke(true.B)
      for (i <- 0 until 4) {
        dut.clock.step()
        dut.io.overflow.expect(false.B)
        dut.io.dataOut.expect((i + 12).U)
      }
      
      //尝试读取超过缓冲区边界的数据
      dut.io.readEn.poke(true.B)
      for (i <- 0 until 5) {
        dut.clock.step()
        dut.io.overflow.expect(true.B)
        dut.io.dataOut.expect(0.U)
      }

    }
  }

}
*/