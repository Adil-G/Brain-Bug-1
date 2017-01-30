package general.chat;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

/**
 * Created by garad on 2016-12-23.
 */
public class synonymsFromPython {
    public static void main(String[] args)
    {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("import sys\nsys.path.append('C:\\Users\\garad\\Documents\\wn2ooo')\nfrom wn2ooo import *\nimport sys");
// execute a function that takes a string and returns a string
        PyObject someFunc = interpreter.get("syns");
        PyObject result = someFunc.__call__(new PyString("much"));
        String realResult = (String) result.__tojava__(String.class);
        System.out.println(realResult);
    }
}
