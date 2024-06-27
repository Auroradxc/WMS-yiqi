import Http from 'axios'
export const login = async (req) => {
  let info;
  await Http.post("http://97.64.20.181:8000/login", req).then((res) => {
    info = res.data;
  })
  return info;
}
export const enstore_list = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_all_enstore_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}

export const delete_enstore_list_entry = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/delete_enstore_list_entry", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_supplement_list = async(request_params) => {
  let info;
  console.log(request_params);
  await Http.post("http://97.64.20.181:8000/request_supplement_list", request_params).then(
      (Response)=>{
          info=Response.data;
      }
  )
  //console.log(info);
  return info;
}

export const get_enstore_by_id = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_enstorelist_detail", req).then((res) => {

    info = res.data;
  })
  return info;
}
export const request_enstore_list = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_enstorelist_detail", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const update_detail_list = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/update_detail_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const update_stock_accordingly = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/update_stock_accordingly", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}



export const get_print_detail = async (req) => {
  let info;
  await Http.post("http://97.64.20.181:8000/request_enstorelist_detail", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_supplement_via_id = async (req) => {
  let info;
  await Http.post("http://97.64.20.181:8000/request_supplement_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_supplier_list = async () => {
  let info;
  await Http.post("http://97.64.20.181:8000/get_supplier_list").then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_stock_list = async () => {
  let info;
  await Http.post("http://97.64.20.181:8000/request_stock").then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const exstore_list = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_all_exstore_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}


export const request_exstorelist_detail = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_exstorelist_detail", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const account_detail_list = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/account_detail_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const delete_exstore_list_entry = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/delete_exstore_list_entry", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const relieve_stock_accordingly = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/relieve_stock_accordingly", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const fetchInventoryItem = async(req) =>{
    let info;
    console.log(req);
    await Http.post("http://97.64.20.181:8000/request_supplement_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const exstorelist_detail =async(req)=>{
  let info;
  await Http.post("http://97.64.20.181:8000/request_exstorelist_detail",req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}



export const get_print_detail_exstore = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_exstorelist_detail", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_customer_list = async () => {
  let info;
  await Http.post("http://97.64.20.181:8000/get_customer_list").then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const get_supplement_via_id_exstore = async (req) => {
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_supplement_list", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
export const fetchStock = async(req) =>{
  let info;
  console.log(req);
  await Http.post("http://97.64.20.181:8000/request_stock_advanced", req).then((res) => {
    console.log(res)
    info = res.data;
  })
  return info;
}
