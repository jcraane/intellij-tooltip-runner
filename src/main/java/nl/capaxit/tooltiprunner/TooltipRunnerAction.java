package nl.capaxit.tooltiprunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;

/**
 * todo Implement (not registered yet).
 * Created by jamiecraane on 20/08/16.
 */
public class TooltipRunnerAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (psiFile != null && psiFile instanceof PsiJavaFile) {
            final Project project = e.getProject();
            final PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        }
    }
}
