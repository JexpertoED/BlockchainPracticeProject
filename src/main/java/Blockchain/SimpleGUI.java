package main.java.Blockchain;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

class SimpleGUI extends JFrame {
    private final Blockchain bc;
    private JLabel name = new JLabel("Имя");
    private JLabel surname = new JLabel("Фамилия");
    private JLabel patronymic = new JLabel("Отчество");
    private JLabel password = new JLabel("Пароль");
    private JTextField nameField = new JTextField(20);
    private JTextField surnameField = new JTextField(20);
    private JTextField patronymicField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JLabel title = new JLabel("Введите ваши данные.");
    private JButton okButton = new JButton("OK");

    public SimpleGUI(Blockchain bc) {
        super("Анкета");
        this.bc = bc;
        //this.setBounds(100,100,750,580);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setFont(new Font("Tahoma", Font.PLAIN, 28));
        name.setFont(new Font("Consolas", Font.PLAIN, 14));
        surname.setFont(new Font("Consolas", Font.PLAIN, 14));
        patronymic.setFont(new Font("Consolas", Font.PLAIN, 14));
        password.setFont(new Font("Consolas", Font.PLAIN, 14));

        title.setHorizontalAlignment(JLabel.CENTER);
        name.setHorizontalAlignment(JLabel.RIGHT);
        surname.setHorizontalAlignment(JLabel.RIGHT);
        patronymic.setHorizontalAlignment(JLabel.RIGHT);
        password.setHorizontalAlignment(JLabel.RIGHT);
        //  name.setBorder(new EmptyBorder(0,0,0,10));

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        Container container1 = new Container();
        container.add(container1, BorderLayout.CENTER);
        container1.setLayout(new GridLayout(4, 3, 10, 0));
        container.add(title, BorderLayout.NORTH);
        Container okContainer = new Container();
        okContainer.setLayout(new BorderLayout());
        container.add(okContainer, BorderLayout.SOUTH);
        okContainer.add(okButton, BorderLayout.EAST);
        container1.add(name);
        container1.add(nameField);
        container1.add(new JLabel(""));
        container1.add(surname);
        container1.add(surnameField);
        container1.add(new JLabel(""));
        container1.add(patronymic);
        container1.add(patronymicField);
        container1.add(new JLabel(""));
        container1.add(password);
        container1.add(passwordField);
        container1.add(new JLabel(""));
        nameField.requestFocus();
        okButton.setEnabled(false);


        DocumentListener docListen = new DocumentListener() {

            private int count = 0;

            public void changedUpdate(DocumentEvent e) {
                changed(e);
                //    System.out.println("1");
            }

            public void removeUpdate(DocumentEvent e) {
                changed(e);
                //   System.out.println("2");
            }

            public void insertUpdate(DocumentEvent e) {
                changed(e);
                //   System.out.println("3");
            }

            void changed(DocumentEvent e) {
                if (nameField.getText().equals("") || surnameField.getText().equals("") || passwordField.getPassword().length == 0)
                    okButton.setEnabled(false);

                else
                    okButton.setEnabled(true);

            }
        };
        nameField.getDocument().addDocumentListener(docListen);
        surnameField.getDocument().addDocumentListener(docListen);
        passwordField.getDocument().addDocumentListener(docListen);

        passwordField.setEchoChar('A');

        okButton.addActionListener(e -> {
            this.setVisible(false);
            JFrame frame = new JFrame("Выберите кандидата");
            JLabel candTitle = new JLabel("Кандидаты:");
            candTitle.setHorizontalAlignment(SwingConstants.CENTER);
            JRadioButton b1 = new JRadioButton("Иванов Иван Иванович");
            JRadioButton b2 = new JRadioButton("Сидоров Николай Петрович");
            JRadioButton b3 = new JRadioButton("Петров Сергей Васильевич");
            JButton ok = new JButton("OK");
            candTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
            b1.setFont(new Font("Consolas", Font.PLAIN, 14));
            b2.setFont(new Font("Consolas", Font.PLAIN, 14));
            b3.setFont(new Font("Consolas", Font.PLAIN, 14));

            Container base = frame.getContentPane();
            base.setLayout(new BorderLayout());
            Container base1 = new Container();
            base.add(candTitle, BorderLayout.NORTH);
            base.add(base1, BorderLayout.CENTER);
            base1.setLayout(new GridLayout(3, 3, 10, 0));
            Container okButtonContainer = new Container();
            okButtonContainer.setLayout(new BorderLayout());
            base.add(okButtonContainer, BorderLayout.SOUTH);
            okButtonContainer.add(ok, BorderLayout.EAST);
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(b1);
            buttonGroup.add(b2);
            buttonGroup.add(b3);
            b1.setSelected(true);
            base1.add(new JLabel(""));
            base1.add(b1);
            base1.add(new JLabel(""));
            base1.add(new JLabel(""));
            base1.add(b2);
            base1.add(new JLabel(""));
            base1.add(new JLabel(""));
            base1.add(b3);
            base1.add(new JLabel(""));
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setAlwaysOnTop(true);

            ok.addActionListener(event -> {
               // System.out.println("asdasd");
                int candidate = b1.isSelected() ? 1 : (b2.isSelected() ? 2 : 3);
               // System.out.println(candidate);
//                try {
//                    KeyPair keyPair = Transaction.generateKeypair(Transaction.getSeed((nameField.getText() + surnameField.getText() + patronymicField.getText()).getBytes(), new String(passwordField.getPassword()).getBytes()));
//                    System.out.println(Blockchain.bytesToHex(keyPair.getPrivate().getEncoded()));
//                    System.out.println(Blockchain.bytesToHex(keyPair.getPublic().getEncoded()));
//                    System.out.println(keyPair.getPublic());
//                } catch (NoSuchAlgorithmException | IOException e1) {
//                    e1.printStackTrace();
//                }
                try {
                    bc.addTransactionToPool(new Transaction(SHA256.sha256("123asd".getBytes()), new String(passwordField.getPassword()).getBytes(),(nameField.getText() + surnameField.getText() + patronymicField.getText()).getBytes(),true,(Transaction.generateKeypair(String.valueOf(candidate).getBytes()).getPublic().getEncoded())));
                } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e1) {
                    e1.printStackTrace();
                    System.out.println("Exception");
                }

            });
        });


        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
    }

    class CandidateFrame {
        private JFrame frame;
        private Container myPanel;
        private GroupLayout groupLayout;
        private GroupLayout.SequentialGroup hGroup;
        private GroupLayout.SequentialGroup vGroup;

        public CandidateFrame() {
            JFrame frame = new JFrame("Выберите кандидата");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container myPanel = frame.getContentPane();
            GroupLayout groupLayout = new GroupLayout(myPanel);
            groupLayout.setAutoCreateGaps(true);
            groupLayout.setAutoCreateContainerGaps(true);
            myPanel.setLayout(groupLayout);
            this.hGroup = groupLayout.createSequentialGroup();
            this.vGroup = groupLayout.createSequentialGroup();
            this.frame = frame;
            this.myPanel = myPanel;
            this.groupLayout = groupLayout;
        }

        public void addCandidate(String name) {

        }

        public JFrame getFrame() {
            return frame;
        }

        public void setFrame(JFrame frame) {
            this.frame = frame;
        }

        public Container getPanel() {
            return myPanel;
        }

        public void setPanel(Container myPanel) {
            this.myPanel = myPanel;
        }

        public GroupLayout getGroupLayout() {
            return groupLayout;
        }

        public void setGroupLayout(GroupLayout groupLayout) {
            this.groupLayout = groupLayout;
        }

        public GroupLayout.SequentialGroup gethGroup() {
            return hGroup;
        }

        public void sethGroup(GroupLayout.SequentialGroup hGroup) {
            this.hGroup = hGroup;
        }

        public GroupLayout.SequentialGroup getvGroup() {
            return vGroup;
        }

        public void setvGroup(GroupLayout.SequentialGroup vGroup) {
            this.vGroup = vGroup;
        }
    }


}