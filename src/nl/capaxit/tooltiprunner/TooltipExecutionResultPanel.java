package nl.capaxit.tooltiprunner;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.panels.NonOpaquePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which displays the runnners execution result in a panel. Inspired by https://github.com/chashnikov/IntelliJ-presentation-assistant.
 * <p>
 * Created by jamiecraane on 09/08/16.
 */
public class TooltipExecutionResultPanel extends NonOpaquePanel {
    private JBPopup result;
    private final Project project;
    private final Editor editor;
    private JPanel resultPanel;
    private final String executionResult;

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
        result.setSize(getPreferredSize());
        result.getContent().repaint();
    }
}
