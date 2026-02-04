package com.imocha.parser.util;

import com.imocha.parser.dto.CallType;
import com.imocha.parser.model.CallRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

@Component
@Slf4j
public class FileNameValidator {

    private static final Pattern PATTERN =
            Pattern.compile("^\\d{3}\\.(\\d{8})\\.TXT$");
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.BASIC_ISO_DATE;
    public static boolean isValidate(ZipEntry entry) {

        String fileName=entry.getName();
        if (entry.isDirectory()){
            log.info("entry is a directory");
            return false;
        }
        if (!fileName.endsWith(".TXT")){
            log.warn("File name:[{}] is not ended by .TXT",fileName);
         return false;
        }

        Matcher matcher = PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            log.error("File name:[{}] is not matched by the pattern [NWID.DATE.TXT]",fileName);
            return false;
        }

        String datePart = matcher.group(1);

        try {
            LocalDate.parse(datePart, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            log.error("Invalid date in file name");
            return false;
        }
    }
}
