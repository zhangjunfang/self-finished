/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.template.parser;

public class SyntaxErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    int col;
    int row;

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public SyntaxErrorException() {
        super();
    }

    public SyntaxErrorException(String message, int col, int row, Throwable cause) {
        super(message, cause);
        this.col=col;
        this.row=row;
    }


    public SyntaxErrorException(String message, int col, int row) {
        super(message);
        this.col=col;
        this.row=row;
    }

    public SyntaxErrorException(Throwable cause) {
        super(cause);
    }
}
