package nl.capaxit.tooltiprunner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by jamiecraane on 06/07/16.
 */
public class TooltipRunnerRunConfiguration extends ApplicationConfiguration {
    protected TooltipRunnerRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(name, project, factory);
    }

    @Override
    public RunProfileState getState(@NotNull final Executor executor, @NotNull final ExecutionEnvironment env) throws ExecutionException {
        final JavaCommandLineState state = new JavaApplicationCommandLineState<ApplicationConfiguration>(this, env);
        JavaRunConfigurationModule module = getConfigurationModule();
        final Editor editor = FileEditorManager.getInstance(getProject()).getSelectedTextEditor();
        state.setConsoleBuilder(new MyConsoleViewBuilder(getProject(), editor));
        return state;
    }

    public static class MyConsoleViewBuilder extends TextConsoleBuilder {
        private Project project;
        private Editor editor;

        public MyConsoleViewBuilder(Project project, Editor editor) {
            this.project = project;
            this.editor = editor;
        }

        @Override
        public ConsoleView getConsole() {
            return new ConsoleViewImpl(project, true) {
                @Override
                public void print(@NotNull String s, @NotNull ConsoleViewContentType contentType) {
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!s.startsWith("/Library") && !s.contains("Process finished")) {
//                                HintManager.getInstance().showInformationHint(editor, s);
//                                todo do not create execution result panel each time.
                                new TooltipExecutionResultPanel(project, s, editor).updateText(project, s);
                            }
                        }
                    });
                }
            };
        }

        @Override
        public void addFilter(Filter filter) {

        }

        @Override
        public void setViewer(boolean isViewer) {

        }
    }
}
