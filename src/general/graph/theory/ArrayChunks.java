package general.graph.theory;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by corpi on 2016-10-23.
 */
public class ArrayChunks {
    public static ArrayList<File[]> chunks(ArrayList<File> bigList, int n){
        ArrayList<File[]> chunks = new ArrayList<File[]>();

        for (int i = 0; i < bigList.size(); i += n) {
            ArrayList<File> files= new ArrayList<File>(bigList.subList(i, Math.min(bigList.size(), i + n)));
            File[] chunk = (File[]) (files.toArray(new File[files.size()]));
            chunks.add(chunk);
        }

        return chunks;
    }
}
