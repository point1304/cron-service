package com.ksyim.hellocron.server.command;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.cron.CronScheduler;

import java.util.List;

import com.linecorp.armeria.client.WebClient;

@Command
@Parameters(separators = "=", commandDescription = "List scheduled tasks.")
public class LsCommand extends AbstractCommand {

    @Parameter(names = {"-a", "--all"}, description = "Show all scheduled tasks.")
    public static boolean all;

    @Parameter(
            names = {"-o", "--only"},
            description = "Show either `cron` or `at` task only. " +
                    "With this flag on, `--all` flag will be ignored.",
            validateWith = OnlyParamValidator.class
    )
    public static String only;

    // TODO: Replace JCommander validation with Hibernate validation.
    public static class OnlyParamValidator implements IParameterValidator {

        private List<String> allowedValues = List.of("at", "cron");

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!allowedValues.contains(value)) {
                throw new ParameterException(
                        getExceptionMessageWithParamName(name));
            }
        }

        public String getExceptionMessageWithParamName(String paramName) {
            StringBuilder sb = new StringBuilder();

            sb.append("Prameter `");
            sb.append(paramName);
            sb.append("` must be either ");
            sb.append("`%s`");
            for (int i = 1; i < allowedValues.size() - 1; i++) {
                sb.append(", `%s`");
            }
            sb.append(" or `%s`");

            return String.format(sb.toString(), allowedValues.toArray());
        }
    }

    @Override
    public void execute(WebClient client, CronScheduler cronScheduler) {

    }
}
