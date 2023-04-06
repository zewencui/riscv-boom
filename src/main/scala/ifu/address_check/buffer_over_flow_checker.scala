import chisel3._
import chisel3.util._
import treadle._

class BufferOverflowCheck(bufferSize: Int, boundaryOffset: Int) extends Module {
  val io = IO(new Bundle {
    val dataIn = Input(UInt(8.W))
    val writeEn = Input(Bool())
    val readEn = Input(Bool())
    val overflow = Output(Bool())
  })

  val buffer = RegInit(VecInit(Seq.fill(bufferSize)(0.U(8.W))))
  val boundary = (bufferSize - boundaryOffset).U
  val index = RegInit(0.U(log2Ceil(bufferSize).W))

  // Write
  when(io.writeEn) {
    index := index + 1.U
    buffer(index) := io.dataIn
  }

  // Read
  val readData = Mux(io.readEn, buffer(boundary), 0.U(8.W))

  // Check
  io.overflow := io.writeEn && (index >= boundary)
}

object BufferOverflowCheckTester extends App {
  val bufferSize = 16
  val boundaryOffset = 4
  val dataSeq = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
  val expectedOverflowSeq = Seq(false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true)
  val writeEnSeq = Seq(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)
  val readEnSeq = Seq(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false)

  Driver.execute(Array("-tbn", "verilator"), () => new BufferOverflowCheck(bufferSize, boundaryOffset)) { c =>
    new PeekPokeTester(c) {
      for (i <- dataSeq.indices) {
        poke(c.io.dataIn, dataSeq(i).toByte)
        poke(c.io.writeEn, writeEnSeq(i))
        poke(c.io.readEn, readEnSeq(i))
        step(1)
        expect(c.io.overflow, expectedOverflowSeq(i))
      }
    }
  }
}






/* iotest这个版本不支持
import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester


class BufferOverflowCheck(bufferSize: Int, boundaryOffset: Int) extends Module {
  val io = IO(new Bundle {
    val dataIn = Input(UInt(8.W))
    val writeEn = Input(Bool())
    val readEn = Input(Bool())
    val overflow = Output(Bool())
  })

  val buffer = RegInit(VecInit(Seq.fill(bufferSize)(0.U(8.W))))
  val boundary = (bufferSize - boundaryOffset).U
  val index = RegInit(0.U(log2Ceil(bufferSize).W))

  // Write
  when(io.writeEn) {
    index := index + 1.U
    buffer(index) := io.dataIn
  }

  // Read
  val readData = Mux(io.readEn, buffer(boundary), 0.U(8.W))

  // Check
  io.overflow := io.writeEn && (index >= boundary)
}

class BufferOverflowCheckTester(c: BufferOverflowCheck) extends PeekPokeTester(c) {
  val bufferSize = 16
  val boundaryOffset = 4
  val dataSeq = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
  val expectedOverflowSeq = Seq(false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true)
  val writeEnSeq = Seq(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)
  val readEnSeq = Seq(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false)

  for (i <- dataSeq.indices) {
    poke(c.io.dataIn, dataSeq(i).toByte)
    poke(c.io.writeEn, writeEnSeq(i))
    poke(c.io.readEn, readEnSeq(i))
    step(1)
    expect(c.io.overflow, expectedOverflowSeq(i))
  }
}

*/

/*
object BufferOverflowCheckMain extends App {
  chisel3.iotesters.Driver(() => new BufferOverflowCheck(16, 4)) { c =>
    new BufferOverflowCheckTester(c)
  }
}    //TODO:
*/