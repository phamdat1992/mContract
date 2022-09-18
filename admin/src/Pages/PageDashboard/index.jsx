import DefaultFooter from "@Containers/Layout/Footer";
import ContractOverview from "@Containers/Dashboard/ContractOverview";
import ContractPolicy from "@Containers/Dashboard/ContractPolicy";
import CountCard from "@Containers/Dashboard/CountCard";
import Standard from "@Containers/Dashboard/Standard";
import DbTop from "@Containers/Dashboard/Top";
import { useEffect } from "react";
import SimpleBar from "simplebar-react";
function PageDashboard(props) {
   useEffect(() => {
      document.title = "MContract";
   }, []);
   return (
      <SimpleBar id="page_content" className="container-fluid px-24px">
         <div className="dashboard_area py-4">
            <DbTop />
            <CountCard />
            <ContractOverview />
            <ContractPolicy />
            <Standard />
         </div>
         <DefaultFooter />
      </SimpleBar>
   );
}

export { PageDashboard };
