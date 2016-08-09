package nl.capaxit.tooltiprunner;

import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiMethodUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by jamiecraane on 06/07/16.
 */
public class TooltipRunConfigurationType implements ConfigurationType {
    @Override
    public String getDisplayName() {
        return "TooltipRunner";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "TooltipRunner";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.General.Information;
    }

    @NotNull
    @Override
    public String getId() {
        return "TOOLTIP_RUNNER_RUNCONFIGURATION";
    }

    private final ConfigurationFactory myFactory;

    public TooltipRunConfigurationType() {
        myFactory = new ConfigurationFactoryEx(this) {
            @Override
            public RunConfiguration createTemplateConfiguration(Project project) {
                return new TooltipRunnerRunConfiguration(project, myFactory, "");
            }

            @Override
            public void onNewConfigurationCreated(@NotNull RunConfiguration configuration) {
                ((ModuleBasedConfiguration)configuration).onNewConfigurationCreated();
            }
        };
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{myFactory};
    }

    @Nullable
    public static PsiClass getMainClass(PsiElement element) {
        while (element != null) {
            if (element instanceof PsiClass) {
                final PsiClass aClass = (PsiClass)element;
                if (PsiMethodUtil.findMainInClass(aClass) != null){
                    return aClass;
                }
            } else if (element instanceof PsiJavaFile) {
                final PsiJavaFile javaFile = (PsiJavaFile)element;
                final PsiClass[] classes = javaFile.getClasses();
                for (PsiClass aClass : classes) {
                    if (PsiMethodUtil.findMainInClass(aClass) != null) {
                        return aClass;
                    }
                }
            }
            element = element.getParent();
        }
        return null;
    }

    @NotNull
    public static ApplicationConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(ApplicationConfigurationType.class);
    }
}
