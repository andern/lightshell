/*
 * Copyright (C) 2012, 2013 Andreas Halle
 *
 * This file is part of lightshell.
 *
 * lightshell is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * lightshell is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public license
 * along with lightshell. If not, see <http://www.gnu.org/licenses/>.
 */
package test.commands;

import lightshell.Command;

public class Warranty extends Command {
    protected String getName() { return "warranty"; }
    protected String getShortHelp() { return "show license warranty"; }
    protected String getUsage() { return "warranty"; }
    
    protected String execute(String arg) {
        return
        "This program is distributed in the hope that it will be useful,\n"
      + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
      + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
      + "GNU General Public License for more details.";
    }
}