package nl.capaxit.tooltiprunner;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.util.Alarm;
import com.intellij.util.ui.Animator;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which displays the runnners execution result in a panel. Inspired by https://github.com/chashnikov/IntelliJ-presentation-assistant.
 * <p>
 * Created by jamiecraane on 09/08/16.
 */
public class TooltipExecutionResultPanel extends NonOpaquePanel {
    private JBPopup result;
    public static final int HIDE_DELAY = 4 * 1000;
    private final Project project;
    private final Editor editor;
    private Alarm hideAlarm = new Alarm();
    private Animator animator;
    private JPanel resultPanel;
    private final String executionResult;
    private Phase phase = Phase.FADING_IN;
    private float hintAlpha = (UIUtil.isUnderDarcula()) ? 0.05F : 0.1F;

    public TooltipExecutionResultPanel(final Project project, final String executionResult, final Editor editor) {
        this.project = project;
        this.editor = editor;
        this.executionResult = executionResult;
        final IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);
        final JBColor background = new JBColor(new Color(186, 238, 186, 120), new Color(73, 117, 73));
        setBackground(background);
        setOpaque(true);
        resultPanel = new NonOpaquePanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(resultPanel, BorderLayout.CENTER);
        result = JBPopupFactory.getInstance().createComponentPopupBuilder(this, this)
                .setAlpha(5.0F)
                .setFocusable(false)
                .setBelongsToGlobalPopupStack(false)
                .setCancelKeyEnabled(false)
                .createPopup();
        resultPanel.add(createLabel(executionResult));
        result.show(computeLocation(ideFrame));

        animator = new FadeInOutAnimator(true);
//        result.show(computeLocation(ideFrame));
        animator.resume();

    }

    private RelativePoint computeLocation(IdeFrame ideFrame) {
        int statusBarHeight = ideFrame.getStatusBar().getComponent().getHeight();
        Rectangle visibleRect = ideFrame.getComponent().getVisibleRect();
        Dimension popupSize = getPreferredSize();
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
//        todo figure out caret position.
        Point point = new Point(visibleRect.x + (visibleRect.width - popupSize.width) / 2, visibleRect.y + visibleRect.height / 2);
        return new RelativePoint(ideFrame.getComponent(), point);
    }

    private Window getHintWindow() {
        if (result.isDisposed()) return null;
        final Window window = SwingUtilities.windowForComponent(result.getContent());
        if (window != null && window.isShowing()) return window;
        return null;
    }

    public JLabel createLabel(final String value) {
        JLabel jLabel = new JLabel("<html>" + value + "</html>", SwingConstants.CENTER);
        return jLabel;
    }

    public void updateText(final Project project, final String executionResult) {
        if (getHintWindow() == null) return;
        resultPanel.removeAll();
        resultPanel.add(createLabel(executionResult));
        result.getContent().invalidate();
        final IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);
        result.setLocation(computeLocation(ideFrame).getScreenPoint());
//        result.setSize(getPreferredSize());
        result.getContent().repaint();
        showFinal();
    }

    private void showFinal() {
        phase = Phase.SHOWN;
        setAlpha(hintAlpha);
        hideAlarm.cancelAllRequests();
        hideAlarm.addRequest(() -> fadeOut(), HIDE_DELAY);
    }

    private void setAlpha(float alpha) {
        Window window = getHintWindow();
        if (window != null) {
            WindowManager.getInstance().setAlphaModeRatio(window, alpha);
        }
    }

    private void fadeOut() {
        if (phase != Phase.SHOWN) return;
        phase = Phase.FADING_OUT;
        Disposer.dispose(animator);
        animator = new FadeInOutAnimator(false);
        animator.resume();
    }

    private class FadeInOutAnimator extends Animator {
        private boolean forward;

        public FadeInOutAnimator(boolean forward) {
            super("Action Hint Fade In/Out", 5, 100, false, forward);
            this.forward = forward;
        }

        public void paintNow(int frame, int totalFrames, int cycle) {
            if (forward && phase != Phase.FADING_IN
                    || !forward && phase != Phase.FADING_OUT) return;
            setAlpha(hintAlpha + (1 - hintAlpha) * (totalFrames - frame) / totalFrames);
        }

        public void paintCycleEnd() {
            if (forward) {
                showFinal();
            } else {
                close();
            }
        }

        private void close() {
            Disposer.dispose(this);
        }

    }


    public enum Phase {FADING_IN, SHOWN, FADING_OUT, HIDDEN}
}
