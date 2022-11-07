package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class page_rep implements ActionListener {

    Font ft= new Font("Comic Sans MS", Font.PLAIN, 20);
    JFrame fm= new JFrame("Page Replacement Techniques");
    JLabel lb1= new JLabel("Select number of frames (2-6)         ");

    Integer a[]= {1,2,3,4,5,6};
    JComboBox jcb= new JComboBox(a);
    JLabel lb2= new JLabel("Add String        ");
    JTextField jtf= new JTextField("7,0,1,2,0,3,0,4,3,0,3 (Use this format for correct result)",150);


    JButton jb= new JButton("Compute");


    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel();
    JPanel jp3 = new JPanel();
    JPanel jp4 = new JPanel();
    JPanel jp5 = new JPanel();
    JPanel jp6 = new JPanel();
    JPanel jp7 = new JPanel();

    int ok;

    page_rep()
    {
        jb.addActionListener(this);
        lb1.setBackground(Color.YELLOW);
        lb2.setBackground(Color.PINK);
        lb1.setFont(ft);
        lb2.setFont(ft);
        jcb.setFont(ft);
        jtf.setFont(ft);
        jb.setFont(ft);

        jcb.setBackground(Color.lightGray);

        jb.setBackground(Color.blue);

        jp1.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp2.setLayout(new BorderLayout());
        jp3.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp5.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jp6.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jp7.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jp1.add(lb1);
        jp1.add(jcb);
        jp2.add(lb2);
        jp2.add(jtf);
        jp3.add(jb);

        fm.add(jp1);
        fm.add(jp2);
        fm.add(jp3);


        Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
        fm.setBounds(0,0,dm.width,dm.height);
        fm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fm.setVisible(true);
        fm.setLayout(new GridLayout(8,1));
        fm.setVisible(true);
    }
    public static void main(String[] args) {
        page_rep pr = new page_rep();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ok = (int)jcb.getSelectedItem();
        String s = jtf.getText();
        s += ',';
        int ln = 0;
        for(int i=0; i<s.length(); i++)
        {
            if(s.charAt(i) == ',')
                ln++;
        }
        String ar[] = new String[ln];
        int id = 0;
        String tmp = "";
        for(int i=0; i<s.length(); i++)
        {
            if(s.charAt(i) == ',')
            {
                ar[id] = tmp;
                tmp = "";
                id++;
                continue;
            }
            tmp += s.charAt(i);
        }

        String fifo[][] = new String[ok][ln];
        String op[][] = new String[ok][ln];
        String lru[][] = new String[ok][ln];

        int pfault_fifo = 0;
        int pfault_op = 0;
        int pfault_lru = 0;

        for(int i=0; i<ok; i++)
        {
            for(int j=0; j<ln; j++)
            {
                fifo[i][j] = "NULL";
            }
        }

        for(int i=0; i<ok; i++)
        {
            for(int j=0; j<ln; j++)
            {
                op[i][j] = "NULL";
            }
        }

        for(int i=0; i<ok; i++)
        {
            for(int j=0; j<ln; j++)
            {
                lru[i][j] = "NULL";
            }
        }

        fifo[0][0] = ar[0];
        int x=0;
        for(int j=1; j<ln; j++)
        {
            boolean found = false;
            for(int k=0; k<ok; k++)
            {
                if(fifo[k][j-1].equals(ar[j]))
                {
                    found = true;
                    break;
                }
            }

            for(int k=0; k<ok; k++)
            {
                fifo[k][j] = fifo[k][j-1];
            }
            if(found == true)
            {
                continue;
            }

            pfault_fifo++;
            boolean f = false;
            for(int k=0; k<ok; k++)
            {
                if(fifo[k][j].equals("NULL"))
                {
                    fifo[k][j] = ar[j];
                    f = true;
                    break;
                }
            }
            if(f == true)
                continue;
            fifo[x][j] = ar[j];
            x++;
            if(x == ok)
                x = 0;
        }
        op[0][0] = ar[0];
        for(int j=1; j<ln; j++)
        {
            boolean found = false;
            for(int k=0; k<ok; k++)
            {
                if(op[k][j-1].equals(ar[j]))
                {
                    found = true;
                    break;
                }
            }
            for(int k=0; k<ok; k++)
            {
                op[k][j] = op[k][j-1];
            }
            if(found == true)
                continue;

            pfault_op++;
            boolean f = false;
            for(int k=0; k<ok; k++)
            {
                if(op[k][j].equals("NULL"))
                {
                    op[k][j] = ar[j];
                    f = true;
                    break;
                }
            }
            if(f == true)
                continue;

            HashMap<String,Integer> hm = new HashMap<>();
            for(int k=0; k<j; k++)
            {
                hm.put(ar[k],1);
            }
            int mx = 0;
            String a = "";
            for(int k=j+1; k<ln; k++)
            {
                if(hm.containsKey(ar[k]))
                {
                    if((k - j) > mx)
                    {
                        mx = (k - j);
                        a = ar[k];
                    }
                }
            }
            for(int k=0; k<ok; k++)
            {
                if(op[k][j-1].equals(a))
                {
                    op[k][j] = ar[j];
                    break;
                }
            }
        }

        lru[0][0] = ar[0];
        for(int j=1; j<ln; j++)
        {
            boolean found = false;
            for(int k=0; k<ok; k++)
            {
                if(lru[k][j-1].equals(ar[j]))
                {
                    found = true;
                    break;
                }
            }
            for(int k=0; k<ok; k++)
            {
                lru[k][j] = op[k][j-1];
            }
            if(found == true)
                continue;

            pfault_lru++;
            boolean f = false;
            for(int k=0; k<ok; k++)
            {
                if(lru[k][j].equals("NULL"))
                {
                    lru[k][j] = ar[j];
                    f = true;
                    break;
                }
            }
            if(f == true)
                continue;

            int mx = 99999;
            String a = "";
            HashMap<String,Integer> hp = new HashMap<>();
            for(int k=0; k<j; k++)
            {
                if(hp.containsKey(ar[k]))
                {
                    hp.put(ar[k],hp.get(ar[k])+1);
                }
                else
                {
                    hp.put(ar[k],1);
                }
                if(hp.get(ar[k]) < mx)
                {
                    mx = hp.get(ar[k]);
                    a = ar[k];
                }
            }
            for(int k=0; k<ok; k++)
            {
                if(lru[k][j-1].equals(a))
                {
                    lru[k][j] = ar[j];
                    break;
                }
            }
        }

        JLabel lb3 = new JLabel("FIRST IN FIRST OUT           PAGE FAULTS = " + pfault_fifo);
        JLabel lb4 = new JLabel("OPTIMAL PAGE REPLACEMENT           PAGE FAULTS = " + pfault_op);
        JLabel lb5 = new JLabel("LEAST RECENTLY USED           PAGE FAULTS = " + pfault_lru);

        JTable jt1 = new JTable(fifo,ar);
        //jt1.setBounds(50,50,100,2000);

        JTable jt2 = new JTable(op,ar);
        //jt2.setBounds(50,50,100,2000);

        JTable jt3 = new JTable(lru,ar);
        //jt3.setBounds(50,50,100,2000);

        JScrollPane jsp1 = new JScrollPane(jt1);
        JScrollPane jsp2 = new JScrollPane(jt2);
        JScrollPane jsp3 = new JScrollPane(jt3);

        lb3.setFont(ft);
        lb4.setFont(ft);
        lb5.setFont(ft);
        
        jp5.add(lb3);
        fm.add(jp5);
        fm.add(jsp1);

        jp6.add(lb4);
        fm.add(jp6);
        fm.add(jsp2);

        jp7.add(lb5);
        fm.add(jp7);
        fm.add(jsp3);
    }
}