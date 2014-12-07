/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphstream.ui.view;

/**
 * view timer factory
 *
 * @author trajar
 */
public interface ViewTimerFactory
{
    ViewTimer create(int delayMs, Runnable worker);
    void close();
}
