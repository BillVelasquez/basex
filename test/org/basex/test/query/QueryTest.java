package org.basex.test.query;

import org.basex.core.Context;
import org.basex.core.Process;
import org.basex.core.Prop;
import org.basex.core.proc.Close;
import org.basex.core.proc.CreateDB;
import org.basex.core.proc.DropDB;
import org.basex.core.proc.XPath;
import org.basex.core.proc.XQuery;
import org.basex.data.Nodes;
import org.basex.data.Result;
import org.basex.query.xquery.XQResult;

/**
 * XPath Test class.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class QueryTest {
  /** Database Context. */
  private static final Context CONTEXT = new Context();
  /** Test instances. */
  private static final AbstractTest[] TESTS = {
    new SimpleTest(), new XPathMarkFTTest(), new FTTest()
  };

  /**
   * Main method of the test class.
   * @param args command line arguments (ignored)
   */
  public static void main(final String[] args) {
    new QueryTest();
  }

  /**
   * Constructor.
   */
  private QueryTest() {
    Prop.textindex = true;
    Prop.attrindex = true;
    Prop.chop = true;

    boolean ok = true;

    // testing all kinds of combinations
    for(int x = 0; x < 2; x++) {
      for(int a = 0; a < 2; a++) { Prop.ftindex = a == 0;
        for(int b = 0; b < 2; b++) { Prop.ftittr = b == 0;
          for(int c = 0; c < 2; c++) { Prop.ftfuzzy = c == 0;
            for(int d = 0; d < 2; d++) { Prop.ftst = d == 0;
              for(int e = 0; e < 2; e++) { Prop.ftdc = e == 0;
                for(int f = 0; f < 2; f++) { Prop.ftcs = f == 0;
                  ok &= test(x != 0);
                }
              }
            }
          }
        }
      }
    }
    
    /* single test
    Prop.ftindex = true;
    Prop.ftittr = true;
    Prop.ftfuzzy = true;
    Prop.ftst = true;
    Prop.ftdc = true;
    Prop.ftcs = true;
    ok &= test(true);
    */

    System.out.println(ok ? "All tests correct.\n" : "Wrong results...\n");
  }

  /**
   * Tests the specified query implementation.
   * @param xquery use xpath/xquery
   * @return true if everything went alright
   */
  private boolean test(final boolean xquery) {
    boolean ok = true;
    for(final AbstractTest test : TESTS) {
      ok &= test(xquery, test, test.details());
    }
    return ok;
  }

  /**
   * Tests the specified instance.
   * @param xquery use xpath/xquery
   * @param test instance
   * @param ext extended error info
   * @return true if everything went alright
   */
  private boolean test(final boolean xquery, final AbstractTest test,
      final String ext) {
    boolean ok = true;
    final String file = test.doc.replaceAll("\\\"", "\\\\\"");
    Process proc = new CreateDB(file);
    if(!proc.execute(CONTEXT)) {
      err(proc.info(), null);
      return false;
    }

    for(final Object[] qu : test.queries) {
      final boolean correct = qu.length == 3;
      final String query = qu[correct ? 2 : 1].toString();

      proc = xquery ? new XQuery(query) : new XPath(query);
      if(proc.execute(CONTEXT)) {
        Result val = proc.result();
        if(xquery) val = ((XQResult) val).xpResult(CONTEXT.data());

        final Result cmp = correct ? (Result) qu[1] : null;
        if(val instanceof Nodes && cmp instanceof Nodes) {
          ((Nodes) cmp).data = ((Nodes) val).data;
        }
        if(!correct || !val.same(cmp)) {
          err(qu[0] + ": " + (xquery ? "xquery " : "xpath ") + query,
              "  Right: " + (correct ? qu[1] : "error") + "\n  Found: " +
              val + (ext != null ? "\n  Flags: " + ext : ""));
          ok = false;
          continue;
        }
      } else if(correct) {
        err(qu[0].toString(), proc.info() +
            (ext != null ? "\n  Flags: " + ext : ""));
        ok = false;
      }
    }

    final String db = CONTEXT.data().meta.dbname;
    new Close().execute(CONTEXT);
    DropDB.drop(db);
    return ok;
  }

  /**
   * Print specified string to standard output.
   * @param info short info
   * @param detail detailed info
   */
  private void err(final String info, final String detail) {
    System.out.println("- " + info);
    if(detail != null) System.out.println(detail);
  };
}
