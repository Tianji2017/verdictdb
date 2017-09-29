/*
 * Copyright 2017 University of Michigan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.umich.verdict.tpch;

import java.io.FileNotFoundException;

import edu.umich.verdict.TestBase;
import edu.umich.verdict.VerdictConf;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictJDBCContext;
import edu.umich.verdict.exceptions.VerdictException;

public class TpchQuery17 {

    public static void main(String[] args) throws FileNotFoundException, VerdictException {
        VerdictConf conf = new VerdictConf();
        conf.setDbms("impala");
        conf.setHost(TestBase.readHost());
        conf.setPort("21050");
        conf.setDbmsSchema("tpch1g");
        conf.set("loglevel", "debug");

        VerdictContext vc = VerdictJDBCContext.from(conf);
        String sql = "select sum(l_extendedprice) / 7.0 as avg_yearly\n" + "from lineitem\n" + "     inner join (\n"
                + "             select l_partkey as partkey, 0.2 * avg(l_quantity) as small_quantity\n"
                + "             from lineitem inner join part on l_partkey = p_partkey\n"
                + "             group by l_partkey) t\n" + "       on l_partkey = partkey\n" + "     inner join part\n"
                + "       on l_partkey = p_partkey\n" + "where p_brand = 'Brand#23' and\n"
                + "      p_container = 'MED BOX' and\n" + "      l_quantity < small_quantity;\n";
        vc.executeJdbcQuery(sql);

        vc.destroy();
    }

}
