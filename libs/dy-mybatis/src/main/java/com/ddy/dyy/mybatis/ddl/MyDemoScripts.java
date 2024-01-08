package com.ddy.dyy.mybatis.ddl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * .
 */
@Component
public class MyDemoScripts implements ISqlScripts {

    @Override
    public List<String> getSqlScripts() {
//        String sql = "INSERT INTO `t_movie2` (`id`, `code`, `title`, `title2`, `cover`, `cover_thumb`, `girl`, " +
//                "`tags`, `summary`, `extra`, `publish_time`, `status`, `deleted`, `gmt_create`, `gmt_modified`)\n" +
//                "VALUES\n" +
//                "\t(2, 'KTKC-071', '外歩けば毎日セクハラ三昧の頼まれると断れない177cm高身長Hカップグラマー素人さんにエロドッキリ仕掛けたらスゴいHな画が撮れたので発売致します。 辻井ほのか'," +
//                " 'KTKC-071:辻井穗乃果(辻井ほのか)最好看的电影作品参数资料详情(特辑1615期)', 't00000002b.jpg', 't00000002.jpg', '辻井穗乃果', '巨乳,苗条,素人,中,妄想', '作品番号KTKC-071的发行日期为2019-12-19，时长145分钟，本番定义于巨乳,苗条,素人,中出,妄想,族，值得期待敬请关注。', '{\\\"发行商\\\":\\\"巨乳\\\",\\\"时长\\\":\\\"145分钟\\\",\\\"导演\\\":\\\"—-\\\",\\\"作品封面\\\":\\\"\\\",\\\"制作商\\\":\\\"キチックス/妄想族\\\",\\\"类型\\\":\\\"有码\\\"}', '2019-12-19', 1, 0, '2022-10-10 00:18:44', '2022-10-10 00:18:44');\n";
        return Arrays.asList(
                "classpath:db_version/init.sql",
                "classpath:db_version/v1.sql",
//                "sql:" + sql,
                "classpath:db_version/v2.sql"
        );
    }
}