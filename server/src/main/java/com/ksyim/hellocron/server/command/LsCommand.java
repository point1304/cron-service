package com.ksyim.hellocron.server.command;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.List;

@Command
@Parameters(separators = "=", commandDescription = "List scheduled tasks.")
public class LsTask {

    @Parameter(names = {"-a", "--all"}, description = "Show all scheduled tasks.")
    public boolean all = true;

    @Parameter(
            names = {"-o", "--only"},
            description = "Show either `cron` or `at` task only. " +
                    "Having this flag on automatically disables `--all` flag.",
            validateWith = OnlyParamValidator.class
    )
    public String only;

    private static class OnlyParamValidator implements IParameterValidator {

        static private List<String> allowedValues = List.of("at", "cron");

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!allowedValues.contains(value)) {
                throw new ParameterException(
                        getExceptionMessageWithParamName(name));
            }
        }

        public String getExceptionMessageWithParamName(String paramName) {
            StringBuilder sb = new StringBuilder();

            sb.append("Parameter `%s` must be either ");
            sb.append("`%s`");
            for (int i = 1; i < allowedValues.size() - 1; i++) {
                sb.append(", `%s`");
            }
            sb.append("or `%s`");

            return String.format(sb.toString(), paramName, allowedValues);
        }
    }
}
