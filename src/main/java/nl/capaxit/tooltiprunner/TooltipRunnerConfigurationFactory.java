package nl.capaxit.tooltiprunner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by jamiecraane on 06/07/16.
 */
public class TooltipRunnerConfigurationFactory extends ConfigurationFactory {
    protected TooltipRunnerConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new TooltipRunnerRunConfiguration(project, this, "TooltipRunner");
    }
}
