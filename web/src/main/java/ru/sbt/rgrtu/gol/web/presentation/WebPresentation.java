package ru.sbt.rgrtu.gol.web.presentation;

import ru.sbt.rgrtu.gol.presentation.Presentation;

public abstract class WebPresentation implements Presentation {

    protected static final String PAGE_TEMPLATE =
            "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "body {background-color: black; color:gray}\n" +
                    "table {border-spacing: 0px;}\n" +
                    "td { height:10px; width:10px; border: 1px solid; border-color: 202020; }\n" +
                    ".alive {background-color: green;}\n" +
                    ".dead {background-color: black;}\n" +
                    "</style>\n" +
                    "%1$s\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "%2$s\n%3$s\n%4$s" +
                    "</body>\n" +
                    "</html>";
    protected static final String HEADER_TEMPLATE =
            "<h1>Generation: %1$05d</h1>";
    protected static final String FOOTER_TEMPLATE =
            "<p>generated in %1$06dms</p>";
    protected static final String BOARD_TEMPLATE =
            "<table>\n" +
                    "<tbody>\n" +
                    "%1$s\n" +
                    "</tbody>\n" +
                    "</table>";
    protected static final String ROW_TEMPLATE =
            "<tr>%1$s</tr>\n";
    protected static final String LIVE_CELL =
            "<td class=\"alive\"></td>";
    protected static final String DEAD_CELL =
            "<td class=\"dead\"></td>";

}
