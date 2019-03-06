package com.deflatedpickle.windowhangers

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.joml.Vector2i
import java.util.concurrent.Executors


/**
 * Sticks child windows to a root window on the sides defined by {@link #hookPoints hookPoints}
 */
// TODO: Add methods to add and remove attached windows
class WindowHanger(private val rootWindow: WinDef.HWND,
                   private val attachedWindows: MutableMap<String, WinDef.HWND>,
                   /**
                    * Defines the the side a window will hook to
                    */
                   @Suppress("MemberVisibilityCanBePrivate") val hookPoints: MutableMap<String, HookPoint>) {
    private val rootRect = WinDef.RECT()
    private val attachedRect = mutableMapOf<String, WinDef.RECT>()

    private val oldPosition = Vector2i()
    private val newPosition = Vector2i()

    private val executor = Executors.newSingleThreadExecutor()!!

    init {
        for ((k, _) in attachedWindows) {
            attachedRect[k] = WinDef.RECT()
            User32.INSTANCE.GetWindowRect(attachedWindows[k], attachedRect[k])
        }
    }

    /**
     * Runs a thread that hooks the child windows to the root one
     */
    fun run() {
        executor.execute {
            while (true) {
                oldPosition.set(rootRect.left, rootRect.top)
                User32.INSTANCE.GetWindowRect(rootWindow, rootRect)
                newPosition.set(rootRect.left, rootRect.top)

                WindowUtil.movementSpeed.set(oldPosition.x - newPosition.x, oldPosition.y - newPosition.y)
                WindowUtil.windowPosition.set(newPosition.x, newPosition.y)

                val rootWidth = rootRect.right - rootRect.left
                val rootHeight = rootRect.bottom - rootRect.top

                WindowUtil.windowSize.set(rootWidth, rootHeight)

                for ((k, _) in attachedRect) {
                    val attachedWidth = attachedRect[k]!!.right - attachedRect[k]!!.left
                    val attachedHeight = attachedRect[k]!!.bottom - attachedRect[k]!!.top

                    when (hookPoints[k]) {
                        HookPoint.Top -> {
                            User32.INSTANCE.MoveWindow(attachedWindows[k],
                                    rootRect.left,
                                    rootRect.top - attachedHeight /* + 8 */,
                                    rootWidth,
                                    attachedHeight,
                                    true)
                        }
                        HookPoint.Right -> {
                            User32.INSTANCE.MoveWindow(attachedWindows[k],
                                    rootRect.left + rootWidth /* - 15 */,
                                    rootRect.top,
                                    attachedWidth,
                                    rootHeight,
                                    true)
                        }
                        HookPoint.Bottom -> {
                            User32.INSTANCE.MoveWindow(attachedWindows[k],
                                    rootRect.left,
                                    rootRect.top + rootHeight /* - 8 */,
                                    rootWidth,
                                    attachedHeight,
                                    true)
                        }
                        HookPoint.Left -> {
                            User32.INSTANCE.MoveWindow(attachedWindows[k],
                                    rootRect.left - attachedWidth /* + 15 */,
                                    rootRect.top,
                                    attachedWidth,
                                    rootHeight,
                                    true)
                        }
                        HookPoint.Centre -> {
                            User32.INSTANCE.MoveWindow(attachedWindows[k],
                                    rootRect.left + (rootWidth / 2) - (attachedWidth / 2),
                                    rootRect.top + (rootHeight / 2) - (attachedHeight / 2),
                                    attachedWidth,
                                    attachedHeight,
                                    true)
                        }
                    }
                }
            }
        }
    }
}