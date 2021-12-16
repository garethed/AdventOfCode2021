import java.lang.Integer.parseInt
import java.util.*

class BitStream(private var bitSet: BitSet) {
    var offset = 0

    fun readBits(length:Int): Int {
        var output = 0
        for (j in 0 until length) {
            output = output.shl(1)
            if (bitSet.get(offset + j)) output++
        }
        offset += length
        return output
    }

    override fun toString(): String {
        var sb = StringBuilder()
        for (i in 0 until bitSet.length()) {
            sb.append( if (bitSet.get(i)) "1" else "0")
        }
        return sb.toString()
    }

}

class Packet (private var input: BitStream) {

    var packets = mutableListOf<Packet>()
    var type:Int = 0
    var value:Long = 0
    var version:Int = 0

    init {

        fun readLiteral(): Long {
            var output = 0L;

            do {
                var isFinal = input.readBits(1)
                output = output.shl(4)
                output += input.readBits(4)
            }
            while (isFinal == 1)

            return output
        }

        version = input.readBits(3)
        type = input.readBits(3)

        if (type == 4) {
            value = readLiteral()
        }
        else {

            val lengthType = input.readBits(1)
            if (lengthType == 0) {
                val endOffset = input.readBits(15) + input.offset

                do {
                    packets.add(Packet(input))

                } while (input.offset < endOffset)
            }
            else {
                for (i in 0 until input.readBits(11)) {
                    packets.add(Packet(input))
                }
            }
        }
    }

    fun subtree() : Sequence<Packet> {
        return sequenceOf(this)
                .plus(packets.flatMap { it.subtree() })
    }

    fun calc() : Long {
        return when(type) {
            0 -> packets.sumOf { it.calc() }
            1 -> packets.fold(1) { acc, packet -> acc * packet.calc() }
            2 -> packets.minOf { it.calc() }
            3 -> packets.maxOf { it.calc() }
            4 -> value
            5 -> if (packets[0].calc() > packets[1].calc()) 1 else 0
            6 -> if (packets[0].calc() < packets[1].calc()) 1 else 0
            7 -> if (packets[0].calc() == packets[1].calc()) 1 else 0
            else -> throw Exception()

        }
    }
}


class Day16 : DayWithInputFile<Long, Packet>() {

    override fun parseInput(input: String): Packet {

        var i = 0;
        var bitSet = BitSet()

        input.forEach {
            var next = it.toString().toLong(16)
            bitSet.set(i++, (next and 0b1000) > 0L)
            bitSet.set(i++, (next and 0b100) > 0L)
            bitSet.set(i++, (next and 0b10) > 0L)
            bitSet.set(i++, (next and 0b1) > 0L)
        }

        return Packet(BitStream(bitSet))
    }

    override fun part1Impl(input: Packet): Long {
        return input
            .subtree().sumOf { it.version }.toLong()

    }

    override fun part2Impl(input: Packet): Long {
        return input.calc()
    }

    override fun tests() {
        test(::part1Impl, 6L, "D2FE28")
        test(::part1Impl, 16, "8A004A801A8002F478")
        test(::part1Impl, 12, "620080001611562C8802118E34")
        test(::part1Impl, 23, "C0015000016115A2E0802F182340")
        test(::part1Impl, 31, "A0016C880162017C3686B18A3D4780")
        test(::part2Impl, 1L, "9C0141080250320F1802104A08")
    }
}