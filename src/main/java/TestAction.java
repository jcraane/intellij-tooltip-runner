import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

/**
 * Created by jamiecraane on 06/07/16.
 */
public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
//        Messages.showInfoMessage("HELLO", "FROM PLUGIN");
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        System.out.println("editor = " + editor);
        if (editor != null) {
            HintManager.getInstance().showInformationHint(editor, "Hallo from plugin");
//            showInformationHint(editor, "Hello from plugin");
        }

//        ExecutionManager.getInstance(project).
    }
}
