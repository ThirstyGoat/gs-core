/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphstream.ui.swingViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.graphstream.ui.view.ViewTimer;
import org.graphstream.ui.view.ViewTimerFactory;

/**
 * a swing timer factory
 *
 * @author trajar
 */
public class SwingViewTimerFactory implements ViewTimerFactory
{
    @Override
    public ViewTimer create(int delayMs, Runnable worker)
    {
        final TimerImpl timer = new TimerImpl(delayMs, worker);
        timer.timer.start();
        return timer;
    }

    @Override
    public void close()
    {
        // nothing to do
    }

    private static class TimerImpl implements ViewTimer, ActionListener
    {
        private final Timer timer;

        private final Runnable worker;

        public TimerImpl(final int delayMs, final Runnable r)
        {
            if (null == r)
            {
                throw new IllegalArgumentException("Runnable worker cannot be null.");
            }
            this.timer = new Timer(delayMs, this);
            this.timer.setCoalesce(true);
            this.timer.setRepeats(true);
            this.worker = r;
        }

        @Override
        public void stop()
        {
            this.timer.removeActionListener(this);
            this.timer.stop();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            this.worker.run();
        }
    }
}
