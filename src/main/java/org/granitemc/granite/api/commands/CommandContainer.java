/**
*
*/
package org.granitemc.granite.api.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class was created as a part of Granite

 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class CommandContainer {

	private Command annoteCommand;
	private Method method;
	/**
	 * 
	 */
	public CommandContainer(Method method) {
		annoteCommand = method.getAnnotation(Command.class);
	}

	/**
	 * @return
	 */
	public String[] getAliases() {
		return annoteCommand.aliases();
	}
	
	public String getName() {
		return annoteCommand.name();
	}
	
	public String getInfo() {
		return annoteCommand.info();
	}
	
	public Object invoke(CommandInfo info) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		return method.invoke(method.getDeclaringClass().newInstance(), info);
	}

}
