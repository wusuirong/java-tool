/*
 * MicroFocus.com Inc.
 * Copyright(c) 2021 All Rights Reserved.
 */
package org.danny.tools.tmp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wusui
 * @version $Id: 验证reconciliationPriority规则.java, 2021-02-01 10:48 AM wusui Exp $
 */
public class 验证reconciliationPriority规则 {

    public static void main(String[] args) {
        Set<String> objList = new HashSet<>();
        Set<String> subItemList = new HashSet<>();

        String objItemStr = "\t<reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_HC_BellMobility_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_FLAT_InventoryDiscoveryWizard_Inventory Discovery by Manual Scanner Deployment\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_INV_CITS_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_NW_Main_HC_NW_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_VMware_CITS_VMware VirtualCenter Topology by VIM\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_VMware_Citrix_VMware VirtualCenter Topology by VIM\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Inventory Discovery by Manual Scanner Deployment\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_INV_Citrix_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_NW_Main_INV_NW_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_ComboINV_ComboScan_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_INV_AI_Lab_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_INV_BM_InventoryDiscoveryWizard_Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"OSKernelByShell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_VMWare BellMobility_VMware VirtualCenter Topology by VIM\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"VMware VirtualCenter Topology by VIM\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_HC_AI_Lab_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Inventory Discovery by Scanner\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_HC_Citrix_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_BA_Main_BA_Main_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_HC_CITS_Host Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_VMware_AI_Lab_VMware VirtualCenter Topology by VIM\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"J2EE WebSphere by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_WebApp_AI_Lab_J2EE WebSphere by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_WebApp_BellMobility_J2EE WebSphere by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_WebApp_Citrix_J2EE WebSphere by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_WebApp_CITS_J2EE WebSphere by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_WebApp_AI_Lab_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_WebApp_BellMobility_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_WebApp_Citrix_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_Weblogic_CITS_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"JBoss by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Oracle Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Oracle Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_WebApp_AI_Lab_J2EE JBoss by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_HR_A_AI_Lab_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_DB_AI_Lab_Oracle Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_DB_AI_Lab_Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_DB_AI_Lab_Oracle Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_WebApp_CITS_J2EE JBoss by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_HR_A_CITS_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_DB_CITS_Oracle Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_DB_CITS_Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_DB_CITS_Oracle Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_WebApp_Citrix_J2EE JBoss by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_HR_A_Citrix_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_DB_Citrix_Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_DB_Citrix_Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_DB_Citrix_Oracle Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_WebApp_BellMobility_J2EE JBoss by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_HR_A_BellMobility_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_DB_BellMobility_Oracle Connection by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_DB_BellMobility_Oracle Database Connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_DB_BellMobility_Oracle Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MSSQL Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MSSQL Server connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_HR_A_AI_Lab_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_DB_A_AI_Lab_MSSQL Server connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_DB_A_AI_Lab_MSSQL Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_HR_A_CITS_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_DB_A_CITS_MSSQL Server connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_DB_A_CITS_MSSQL Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_HR_A_Citrix_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_DB_Citrix_MSSQL Server connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_DB_Citrix_MSSQL Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_HR_A_BellMobility_Host Applications by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_DB_BellMobility_MSSQL Server connection by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_DB_BellMobility_MSSQL Topology by SQL\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"JEE Weblogic by Shell PSU Discovery\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_WebApp_AI_Lab_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_Weblogic_CITS_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_WebApp_Citrix_J2EE Weblogic by Shell\" priority=\"100\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_WebApp_BellMobility_J2EE Weblogic by Shell\" priority=\"100\"/>";

        String subItemStr = "<reconciliation-priority dataStoreName=\"JEE Weblogic by Shell PSU Discovery\" priority=\"110\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_AI_Lab_WebApp_AI_Lab_J2EE Weblogic by Shell\" priority=\"150\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_CITS_Weblogic_CITS_J2EE Weblogic by Shell\" priority=\"150\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_Citrix_WebApp_Citrix_J2EE Weblogic by Shell\" priority=\"150\"/>\n" +
                "    <reconciliation-priority dataStoreName=\"MZ_IT_BellMobility_WebApp_BellMobility_J2EE Weblogic by Shell\" priority=\"150\"/>";

        String[] strs = objItemStr.split("\\n");
        for (String line : strs) {
            objList.add(line.substring(line.indexOf("dataStoreName=\"")+15, line.indexOf("\" priority")));
        }

        String[] strs2 = subItemStr.split("\\n");
        for (String line : strs2) {
            subItemList.add(line.substring(line.indexOf("dataStoreName=\"")+15, line.indexOf("\" priority")));
        }


        if (include(objList, subItemList)) {
            System.out.println("pass");
        } else {
            System.out.println("fail");
        }
    }

    static boolean include(Set<String> all, Set<String> part) {
        int matchCount = 0;
        for (String str1 : part) {
            for (String str2 : all) {
                if (str1.equals(str2)) {
                    matchCount++;
                    break;
                }
            }
        }
        return matchCount == part.size();
    }

}
