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
                   private val attachedWindow: WinDef.HWND,
                   /**
                    * Defines the the side a window will hook to
                    */
                   @Suppress("MemberVisibilityCanBePrivate") val hookPoint: HookPoint) {
    private val rootRect = WinDef.RECT()
    private val attachedRect = WinDef.RECT()

    init {
        User32.INSTANCE.GetWindowRect(attachedWindow, attachedRect)
    }

    /**
     * Moves the child windows to their parent
     */
    fun moveChildren() {
        User32.INSTANCE.GetWindowRect(rootWindow, rootRect)

        val rootWidth = rootRect.right - rootRect.left
        val rootHeight = rootRect.bottom - rootRect.top

        val attachedWidth = attachedRect.right - attachedRect.left
        val attachedHeight = attachedRect.bottom - attachedRect.top

        when (hookPoint) {
            HookPoint.TOP_CENTRE -> {
                User32.INSTANCE.MoveWindow(attachedWindow,
                        rootRect.left,
                        rootRect.top - attachedHeight /* + 8 */,
                        rootWidth,
                        attachedHeight,
                        true)
            }
            HookPoint.MIDDLE_RIGHT -> {
                User32.INSTANCE.MoveWindow(attachedWindow,
                        rootRect.left + rootWidth /* - 15 */,
                        rootRect.top,
                        attachedWidth,
                        rootHeight,
                        true)
            }
            HookPoint.BOTTOM_CENTRE -> {
                User32.INSTANCE.MoveWindow(attachedWindow,
                        rootRect.left,
                        rootRect.top + rootHeight /* - 8 */,
                        rootWidth,
                        attachedHeight,
                        true)
            }
            HookPoint.MIDDLE_LEFT -> {
                User32.INSTANCE.MoveWindow(attachedWindow,
                        rootRect.left - attachedWidth /* + 15 */,
                        rootRect.top,
                        attachedWidth,
                        rootHeight,
                        true)
            }
            HookPoint.MIDDLE_CENTRE -> {
                User32.INSTANCE.MoveWindow(attachedWindow,
                        rootRect.left + (rootWidth / 2) - (attachedWidth / 2),
                        rootRect.top + (rootHeight / 2) - (attachedHeight / 2),
                        attachedWidth,
                        attachedHeight,
                        true)
            }
        }
    }
}