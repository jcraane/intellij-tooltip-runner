package nl.capaxit.tooltiprunner;

import com.intellij.execution.application.AbstractApplicationConfigurationProducer;
import com.intellij.execution.application.ApplicationConfiguration;

/**
 * todo implement and register.
 * <p>
 * Created by jamiecraane on 17/08/16.
 */
public class TooltipRunnerProducer extends AbstractApplicationConfigurationProducer<ApplicationConfiguration> {
    public TooltipRunnerProducer() {
        super(TooltipRunConfigurationType.getInstance());
    }
}
