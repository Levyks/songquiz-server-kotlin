package com.levyks.songquiz.services

import com.levyks.songquiz.models.Room
import java.util.Collections
import kotlin.math.pow

val rooms: MutableMap<String, Room> = Collections.synchronizedMap(LinkedHashMap());


/**
 * Generates a random and unique room code with a given length
 */
fun generateRoomCode(length: Int, maxNumberOfTries: Int = 1000000): String? {
    val max = 10.0.pow(length).toInt() - 1;
    var tries = 0;

    do {
        val code = (0..max).random().toString().padStart(length, '0');
        if (!rooms.containsKey(code))
            return code;
        if(++tries >= maxNumberOfTries)
            return null;
    } while (true);
}

/**
 * Creates a new room
 */
fun createRoom(leaderNickname: String): Room {
    val code = generateRoomCode(4) ?: throw Exception("Could not generate a unique room code");
    val room = Room(code, leaderNickname);
    rooms[code] = room;
    return room;
}

/**
 * Gets a room by its code
 */
fun getRoom(code: String): Room? {
    return rooms[code];
}