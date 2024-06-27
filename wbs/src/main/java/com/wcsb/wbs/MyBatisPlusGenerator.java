package com.wcsb.wbs;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyBatisPlusGenerator {
    public static void main(String[] args) {
        GlobalConfig config=new GlobalConfig.Builder()
                .author("Jessica_Clorize")
                .outputDir("D:\\华南理工\\Assignment\\System Development\\Springboot Test\\MyBatisGenerator 06192024")
                .fileOverride()
                .dateType(DateType.TIME_PACK).build();
        PackageConfig packageConfig=new PackageConfig.Builder()
                .parent("com.wcsb.wbs")
                .mapper("mapper")
                .service("service")
                .controller("controller")
                .entity("domain")
                .xml("mapper")
                .build();
        StrategyConfig strategyConfig=new StrategyConfig.Builder()
                .addInclude("customer"
                )
                .enableCapitalMode().entityBuilder()
                .enableLombok().columnNaming(NamingStrategy.underline_to_camel)
                .naming(NamingStrategy.underline_to_camel)
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().serviceBuilder()
                .formatServiceFileName("%sService")
                .controllerBuilder().enableRestStyle()
                .build();
        DataSourceConfig.Builder dataSourceConfigBuilder=new DataSourceConfig.Builder(
                "jdbc:mysql://127.0.0.1:3306/wbs0603?characterEncoding=utf8&useSSL=false",
                "root", "jessica_clorize0619"
        );

        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfigBuilder.build());
        autoGenerator.global(config);
        autoGenerator.packageInfo(packageConfig);
        autoGenerator.strategy(strategyConfig);
        autoGenerator.execute();
    }
}
