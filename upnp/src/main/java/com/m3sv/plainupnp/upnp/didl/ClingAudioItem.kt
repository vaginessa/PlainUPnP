package com.m3sv.plainupnp.upnp.didl

import com.m3sv.plainupnp.upnp.R
import org.fourthline.cling.support.model.item.AudioItem
import org.fourthline.cling.support.model.item.MusicTrack

class ClingAudioItem(item: AudioItem) : ClingDIDLItem(item) {

    override val dataType: String = "audio/*"

    override val description: String
        get() {
            if (didlObject is MusicTrack) {
                val track = didlObject
                return (track.firstArtist?.name?.let { it } ?: "") +
                        (track.album?.let { " - $it" } ?: "")
            }

            return (didlObject as AudioItem).description
        }

    override val count: String
        get() = didlObject.resources
                ?.takeIf { it.isNotEmpty() }
                ?.get(0)
                ?.duration
                ?.let { duration ->
                    duration
                            .split("\\.".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0]
                } ?: ""

    override val icon: Int = R.drawable.ic_action_headphones

}
