// Custom data provider for the Spring backend at /api
// All list endpoints return plain arrays; pagination and filtering are done client-side.

const API_URL = import.meta.env.VITE_API_URL || '/api';

const dataProvider = {
  getList: async (resource: string, params: any) => {
    const res = await fetch(`${API_URL}/${resource}`);
    if (!res.ok) throw new Error(`Error ${res.status} al obtener ${resource}`);
    let data: any[] = await res.json();

    // Client-side filter
    const filters = params.filter || {};
    const filterKeys = Object.keys(filters).filter(
      (k) => filters[k] !== '' && filters[k] != null
    );
    if (filterKeys.length > 0) {
      data = data.filter((item) =>
        filterKeys.every((key) =>
          String(item[key] ?? '')
            .toLowerCase()
            .includes(String(filters[key]).toLowerCase())
        )
      );
    }

    // Client-side sort
    const { field = 'id', order = 'DESC' } = params.sort || {};
    data = [...data].sort((a, b) => {
      const av = a[field],
        bv = b[field];
      if (av == null) return 1;
      if (bv == null) return -1;
      if (av < bv) return order === 'ASC' ? -1 : 1;
      if (av > bv) return order === 'ASC' ? 1 : -1;
      return 0;
    });

    const total = data.length;
    const { page = 1, perPage = 25 } = params.pagination || {};
    const start = (page - 1) * perPage;
    return { data: data.slice(start, start + perPage), total };
  },

  getOne: async (resource: string, params: any) => {
    // Try direct GET /{id}
    const res = await fetch(`${API_URL}/${resource}/${params.id}`);
    if (res.ok) return { data: await res.json() };
    // Fallback: fetch all and find by id (for resources without GET /{id})
    const allRes = await fetch(`${API_URL}/${resource}`);
    if (!allRes.ok) throw new Error(`Error ${allRes.status}`);
    const list: any[] = await allRes.json();
    const item = list.find((d) => String(d.id) === String(params.id));
    if (!item) throw new Error('Registro no encontrado');
    return { data: item };
  },

  getMany: async (resource: string, params: any) => {
    const data = await Promise.all(
      params.ids.map(async (id: any) => {
        try {
          const r = await fetch(`${API_URL}/${resource}/${id}`);
          if (r.ok) return await r.json();
        } catch (_) {}
        return { id };
      })
    );
    return { data };
  },

  getManyReference: async (resource: string, params: any) => {
    const res = await fetch(`${API_URL}/${resource}`);
    if (!res.ok) throw new Error(`Error ${res.status}`);
    let data: any[] = await res.json();
    data = data.filter(
      (item) => String(item[params.target]) === String(params.id)
    );
    const { page = 1, perPage = 25 } = params.pagination || {};
    const start = (page - 1) * perPage;
    return { data: data.slice(start, start + perPage), total: data.length };
  },

  create: async (resource: string, params: any) => {
    const res = await fetch(`${API_URL}/${resource}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(params.data),
    });
    if (!res.ok) {
      const msg = await res.text();
      throw new Error(msg || `Error ${res.status}`);
    }
    return { data: await res.json() };
  },

  update: async (resource: string, params: any) => {
    const res = await fetch(`${API_URL}/${resource}/${params.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(params.data),
    });
    if (!res.ok) throw new Error(`Error ${res.status}`);
    return { data: await res.json() };
  },

  delete: async (resource: string, params: any) => {
    await fetch(`${API_URL}/${resource}/${params.id}`, { method: 'DELETE' });
    return { data: params.previousData as any };
  },

  deleteMany: async (resource: string, params: any) => {
    await Promise.all(
      params.ids.map((id: any) =>
        fetch(`${API_URL}/${resource}/${id}`, { method: 'DELETE' })
      )
    );
    return { data: params.ids };
  },

  updateMany: async (_resource: string, params: any) => {
    return { data: params.ids };
  },
};

export default dataProvider;

