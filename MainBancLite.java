import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MainBancLite {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CredentialListing());
    }
}
