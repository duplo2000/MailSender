package Mailsender;

/**
 * Klassen Attachment representerar en e-postbilaga och består av ett filnamn och binär data.
 * Den används för att kapsla in informationen och innehållet i en bilaga som ska inkluderas i ett e-postmeddelande.
 */
public class Attachment {
    private final String fileName;
    private final byte[] data;

    /**
     * Konstruerar ett Attachment-objekt med det angivna filnamnet och binär data.
     *
     * @param fileName Filnamnet för bilagan.
     * @param data     Binär data som representerar bilagans innehåll.
     */
    public Attachment(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    /**
     * Hämtar filnamnet för bilagan.
     *
     * @return Filnamnet för bilagan.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Hämtar den binära datan som representerar bilagans innehåll.
     *
     * @return Binär data för bilagan.
     */
    public byte[] getData() {
        return data;
    }

}