/**
 * Copyright (C) 2013 Aurélien Chabot <aurelien></aurelien>@chabot.fr>
 *
 *
 * This file is part of DroidUPNP.
 *
 *
 * DroidUPNP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * DroidUPNP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with DroidUPNP.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */

package com.m3sv.plainupnp.upnp.didl

import com.m3sv.plainupnp.data.upnp.DIDLItem
import com.m3sv.plainupnp.upnp.R
import org.fourthline.cling.support.model.item.Item

open class ClingDIDLItem(item: Item) : ClingDIDLObject(item), DIDLItem {

    override val uri: String? get() = didlObject.firstResource?.value

    override val icon: Int = R.drawable.ic_file
}
