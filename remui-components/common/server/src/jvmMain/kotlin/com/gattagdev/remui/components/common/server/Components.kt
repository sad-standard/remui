/*
 * Copyright Dynamic Animation Systems, Stronghold Robotics, and other original others.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.gattagdev.remui.components.common.server

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.gattagdev.remui.components.common.Computable
import com.gattagdev.remui.components.common.RemoteValue
import com.gattagdev.remui.server.RemuiApplier
import com.gattagdev.remui.server.RemuiLocal
import com.gattagdev.remui.server.RemuiNode
import com.gattagdev.remui.server.mutableContext
import kotlin.reflect.typeOf

@Composable
inline fun <reified T> Remote(state: State<T>): Computable<T> {
    val remui = RemuiLocal.current

    val node = remember { RemuiNode.Struct(remui.createStruct(typeOf<RemoteValue<T>>()) as RemoteValue<T>) }
    ComposeNode<RemuiNode.Struct<RemoteValue<T>>, RemuiApplier>(
        factory = { node },
        update  = {
            with(mutableContext) {
                set(state.value) { node.struct::remValue setTo it }
            }
        },
        content = {  }
    )
    return Computable.remote(node.struct)
}

@Composable
inline fun <reified T> Remote(value: () -> T): Computable<T> {
    val remui = RemuiLocal.current

    val node = remember { RemuiNode.Struct<RemoteValue<T>>(remui.createStruct(typeOf<RemoteValue<T>>()) as RemoteValue<T>) }
    val c = value()
    ComposeNode<RemuiNode.Struct<RemoteValue<T>>, RemuiApplier>(
        factory = { node },
        update  = {
            with(mutableContext) {
                set(c) { struct::remValue setTo it }
            }
        },
        content = {  }
    )
    return Computable.remote(node.struct)
}