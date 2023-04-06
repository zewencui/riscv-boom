import chisel3._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}

class MemoryLockTest(dut: MemoryLock) extends PeekPokeTester(dut) {
  val numCores = dut.numCores

  // Test requesting lock
  for {
    i <- 0 until numCores
  } {
    poke(dut.io.requestLock(i), true)
    step(1)
    expect(dut.io.lockGranted(i), i == 0)
    expect(dut.lock, i == 0)
    expect(dut.lockOwner, i)
    poke(dut.io.requestLock(i), false)
    step(1)
  }

  // Test releasing lock
  for {
    i <- 0 until numCores
  } {
    poke(dut.io.requestUnlock(i), true)
    step(1)
    expect(dut.lock, false)
    poke(dut.io.requestUnlock(i), false)
    step(1)
  }
}

class MemoryLockSpec extends FlatSpec with Matchers {
  behavior of "MemoryLock"
  it should "grant locks to cores and release locks when requested" in {
    val numCores = 4
    chisel3.iotesters.Driver(() => new MemoryLock(numCores)) { dut =>
      new MemoryLockTest(dut)
    } should be (true)
  }
}
