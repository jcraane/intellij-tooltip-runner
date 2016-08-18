package nl.capaxit.tooltiprunner;

import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by jamiecraane on 06/07/16.
 */
public class TooltipRunConfigurationType extends ApplicationConfigurationType {
    @Override
    public String getDisplayName() {
        return "TooltipRunner";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "TooltipRunner";
    }

//todo provide new icon.
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

    @NotNull
    public static TooltipRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(TooltipRunConfigurationType.class);
    }
}
