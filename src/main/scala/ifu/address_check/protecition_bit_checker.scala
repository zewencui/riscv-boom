//保护位检查模块

import chisel3._
import chisel3.util._

class ProtectionBitChecker extends Module {
    val io = IO(new Bundle {
        val address = Input(UInt(32.W))
        val isRead = Input(Bool())
        val isWrite = Input(Bool())
        val isExecute = Input(Bool())
        val protectionBits = Input(UInt(3.W))
        val accessError = Output(Bool())
    }) 

    //提取
    val readProtection = io.isRead && io.protectionBits(0)
    val writeProtection = io.isWrite && io.protectionBits(1)
    val executeProtection = io.isExecute && io.protectionBits(2)

    //检查
    val readError = readProtection && !writeProtection && !executeProtection
    val writeError = !readProtection && writeProtection && !executeProtection
    val executeError = !readProtection && !writeProtection && executeProtection

    //输出
    io.accessError := readError || writeError || executeError
}